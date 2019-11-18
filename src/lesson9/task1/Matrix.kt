@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Простая
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {
    if (width <= 0 || height <= 0) throw IllegalArgumentException()
    return MatrixImpl(height, width, e)
}

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int, e: E) : Matrix<E> {
    private val array = MutableList(height) { MutableList(width) { e } }

    init {
        for (row in 0 until height) {
            for (column in 0 until width) {
                array[row][column] = e
            }
        }
    }

    override fun get(row: Int, column: Int): E = get(Cell(row, column))

    override fun get(cell: Cell): E = array[cell.row][cell.column]

    override fun set(row: Int, column: Int, value: E) {
        set(Cell(row, column), value)
    }

    override fun set(cell: Cell, value: E) {
        array[cell.row][cell.column] = value
    }

    override fun equals(other: Any?) = other is MatrixImpl<*> && height == other.height && width == other.width

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        for (row in 0 until height) {
            sb.append("[")
            for (column in 0 until width) {
                sb.append(array[row][column])
                sb.append(", ")
            }
            sb.delete(sb.length - 2, sb.length)
            sb.append("]")
        }
        sb.append("]")
        return "$sb" // or, sb.toString()
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + array.hashCode()
        return result
    }

}


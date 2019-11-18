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
    val result = MatrixImpl<E>(height, width)
    for (i in 1..height) {
        for (j in 1..width) {
            result.set(i, j, e)
        }
    }
    return result
}

/**
 * Средняя сложность
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(override val height: Int, override val width: Int) : Matrix<E> {
    private val map = mutableMapOf<Cell, E>()

    override fun get(row: Int, column: Int): E = get(Cell(row, column))

    override fun get(cell: Cell): E = map[cell] ?: throw IllegalArgumentException()

    override fun set(row: Int, column: Int, value: E) {
        set(Cell(row, column), value)
    }

    override fun set(cell: Cell, value: E) {
        map[cell] = value
    }

    override fun equals(other: Any?) = other is MatrixImpl<*> && height == other.height && width == other.width

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        for (row in 1..height) {
            sb.append("[")
            for (column in 1..width) {
                sb.append(map.get(Cell(row, column)))
                sb.append(", ")
            }
            sb.deleteCharAt(sb.length)
            sb.deleteCharAt(sb.length)
            sb.append("]")
            sb.append(", ")
        }
        sb.deleteCharAt(sb.length)
        sb.deleteCharAt(sb.length)
        sb.append("]")
        return "$sb" // or, sb.toString()
    }

    override fun hashCode(): Int {
        var result = height
        result = 31 * result + width
        result = 31 * result + map.hashCode()
        return result
    }
}


@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import kotlin.math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))

    override fun equals(other: Any?): Boolean = other is Point && x == other.x && y == other.y
    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        return result
    }
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double =
        if (
            center.distance(other.center) <= radius + other.radius
        ) 0.0
        else center.distance(other.center) - radius - other.radius


    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius + 1e-6
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException()
    var maxDistance = 0.0
    var res: Segment? = null
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            if (points[i].distance(points[j]) >= maxDistance) {
                maxDistance = points[i].distance(points[j])
                res = Segment(points[i], points[j])
            }
        }
    }
    return res!!
}


/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val p1 = Point((diameter.end.x + diameter.begin.x) / 2, (diameter.end.y + diameter.begin.y) / 2)
    val radius = diameter.begin.distance(diameter.end) / 2
    return Circle(p1, radius)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle in 0.0..PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val nominator = other.b * cos(angle) - b * cos(other.angle)
        val denominator = sin(angle) * cos(other.angle) - sin(other.angle) * cos(angle)
        if (denominator == 0.0)
            throw IllegalArgumentException()

        val x = nominator / denominator
        val y = if (angle != PI / 2)
            (x * sin(angle) + b) / cos(angle)
        else
            (x * sin(other.angle) + other.b) / cos(other.angle)

        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = lineByPoints(s.begin, s.end)

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val k = (b.y - a.y) / (b.x - a.x)
    return when {
        b.x == a.x -> Line(a, PI / 2)
        k < 0 -> Line(a, atan(k) + PI)
        b.y == a.y -> Line(a, 0.0)
        else -> Line(a, atan(k))
    }
}

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val x = (a.x + b.x) / 2
    val y = (a.y + b.y) / 2
    val angle = if (lineByPoints(a, b).angle >= PI / 2) lineByPoints(a, b).angle - PI / 2
    else lineByPoints(a, b).angle + PI / 2
    return Line(Point(x, y), angle)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException()
    var minLength = Double.MAX_VALUE
    var min1 = circles[0]
    var min2 = circles[1]
    for (i in circles.indices) {
        for (j in i + 1 until circles.size) {
            val distance = circles[i].distance(circles[j])
            if (distance < minLength) {
                minLength = distance
                min1 = circles[i]
                min2 = circles[j]
            }
        }
    }
    return min1 to min2
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val line1 = bisectorByPoints(a, b)
    val line2 = bisectorByPoints(b, c)
    val center = line1.crossPoint(line2)
    val radius = center.distance(a)
    return Circle(center, radius)
}

/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    if (points.isEmpty()) throw IllegalArgumentException()
    if (points.size == 1) return Circle(points[0], 0.0)
    if (points.size == 2) return circleByDiameter(Segment(points[0], points[1]))

    var res = circleByDiameter(diameter(*points))
    val x = res.radius
    var minRadius = if (points.all { res.center.distance(it) - res.radius <= 1e-6 }) x
    else Double.MAX_VALUE

    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            for (k in j + 1 until points.size) {
                if (points[i] == points[j] ||
                    points[i] == points[k] ||
                    points[j] == points[k]
                ) continue
                val xx = lineByPoints(points[i], points[j])
                val yy = lineByPoints(points[j], points[k])
                val cos1 = cos(xx.angle)
                val cos2 = cos(yy.angle)
                if (abs(cos1 - cos2) <= 1e-6) continue
                if ((cos1 == -1.0 && cos2 == 1.0) || (cos2 == -1.0 && cos1 == 1.0)) continue
                val currentCircle = circleByThreePoints(points[i], points[j], points[k])
                if (points.all { currentCircle.center.distance(it) - currentCircle.radius <= 1e-6 } && currentCircle.radius < minRadius) {
                    res = currentCircle
                    minRadius = currentCircle.radius
                }//.contains(it) currentCircle.center.distance(it) - currentCircle.radius <= 1e-6
            }
        }
    }

    return res
}


@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import lesson1.task1.sqr
import kotlin.math.*

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result *= i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int {
    var count = 0
    var num = n
    do {
        num /= 10
        count++
    } while (num > 0)
    return count
}

/**
 * Простая
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var y = 0
    var z = 1
    var result = 0
    if (n == 1) return 1
    for (i in 1 until n) {
        result = y + z
        y = z
        z = result
    }
    return result
}

/**
 * Простая
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int {
    for (i in max(m, n)..m * n) {
        if (i % m == 0 && i % n == 0) return i
    }
    return m * n
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    for (i in 2..n / 2) {
        if (n % i == 0) return i
    }
    return n
}

/**
 * Простая
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int {
    for (i in n / 2 downTo 1) {
        if (n % i == 0) return i
    }
    return 1
}

/**
 * Простая
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean {
    var a = n
    var b = m
    while (a != b) {
        if (a > b) a -= b
        else b -= a
    }
    return (a == 1)
}

/**
 * Простая
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */
fun squareBetweenExists(m: Int, n: Int): Boolean {
    for (i in m..n)
        if (sqrt(i.toDouble()) % 1 == 0.0) return true
    return false
}


/**
 * Средняя
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var y = x
    var count = 0
    when {
        y < 2 -> return 0
        y % 2 == 0 -> {
            y /= 2
            count += 1 + collatzSteps(y)
        }
        else -> {
            y = y * 3 + 1
            count += 1 + collatzSteps(y)
        }
    }
    return count
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    var y = x % (2 * PI)
    val x2 = x % (2 * PI)
    var sin = 0.0
    var n = 0
    var k: Int
    while (abs(y) >= abs(eps)) {
        n++
        sin += y
        k = n * 2 + 1
        y = if (n % 2 == 0) x2.pow(k) / factorial(k)
        else -(x2.pow(k) / factorial(k))
    }
    return sin
}

/**
 * Средняя
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    var y = 1.0
    val x2 = x % (2 * PI)
    var cos = 0.0
    var n = 0
    var k: Int
    while (abs(y) >= abs(eps)) {
        n++
        cos += y
        k = n * 2
        y = if (n % 2 == 0) x2.pow(k) / factorial(k)
        else -(x2.pow(k) / factorial(k))
    }
    return cos
}


/**
 * Средняя
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {
    var y = n
    var m = 0
    var count = 0
    while (y > 0) {
        m = m * 10 + (y % 10)
        count++
        y /= 10
    }
    return m
}

/**
 * Средняя
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean = n == revert(n)

/**
 * Средняя
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    var y = n
    while (y > 9) {
        if (y % 10 != (y % 100) / 10) return true
        y /= 10
    }
    return false
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun countSquareNumber(n: Int): Int {
    var count = 0
    for (i in 1..n) {
        var y = sqr(i)
        var x = 0
        while (y > 0) {
            x++
            y /= 10
        }
        count += x
    }
    return count
}

fun squareSequenceDigit(n: Int): Int {
    val x: Int
    var result = 0
    var y: Int
    var z: Int
    for (i in 1..n) {
        z = countSquareNumber(i)
        y = countSquareNumber(i + 1)
        if (n in (z + 1) until y) {
            x = y - n
            result = (i + 1) * (i + 1)
            for (j in 1..x) {
                result /= 10
            }
            return result % 10
        }
        if (countSquareNumber(i) == n) return sqr(i) % 10
    }
    return result
}

/**
 * Сложная
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun countFibNumber(n: Int): Int {
    var count = 0
    for (i in 1..n) {
        var y = fib(i)
        var x = 0
        while (y > 0) {
            x++
            y /= 10
        }
        count += x
    }
    return count
}

fun fibSequenceDigit(n: Int): Int {
    val x: Int
    var result = 0
    var y: Int
    var z: Int
    for (i in 1..n) {
        z = countFibNumber(i)
        y = countFibNumber(i + 1)
        if (n in (z + 1) until y) {
            x = y - n
            result = fib(i + 1)
            for (j in 1..x) {
                result /= 10
            }
            return result % 10
        }
        if (countFibNumber(i) == n) return fib(i) % 10
    }
    return result
}

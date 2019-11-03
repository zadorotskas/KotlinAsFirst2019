@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import lesson2.task2.daysInMonth

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
val months = listOf(
    "января",
    "февраля",
    "марта",
    "апреля",
    "мая",
    "июня",
    "июля",
    "августа",
    "сентября",
    "октября",
    "ноября",
    "декабря"
)

fun dateStrToDigit(str: String): String {
    val matchResult = ((Regex("""^(\d{1,2}) (.*) (\d+)$""").find(str)) ?: return "").groupValues.drop(1)
    val m = months.indexOf(matchResult[1]) + 1
    return if ((matchResult[0].toInt() !in 1..daysInMonth(
            m, matchResult[2].toInt()
        )) || (matchResult[1] !in months) || (matchResult[2].toInt() < 0)
    ) ""
    else {
        val day = matchResult[0].toInt()
        val year = matchResult[2].toInt()
        String.format("%02d.%02d.%d", day, m, year)
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val matchResult = ((Regex("""^(\d{1,2}).(\d{1,2}).(\d+)$""").find(digital)) ?: return "").groupValues.drop(1)
    return if ((matchResult[0].toInt() !in 1..daysInMonth(
            matchResult[1].toInt(), matchResult[2].toInt()
        )) || (matchResult[1].toInt() !in 1..12) || (matchResult[2].toInt() < 0)
    ) ""
    else {
        val m = months[matchResult[1].toInt() - 1]
        val res = matchResult.toMutableList()
        res[1] = m
        res[0] = res[0].toInt().toString()
        res.joinToString(separator = " ")
    }
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String): String {
    val chars = mutableListOf<String>()
    for (element in phone) {
        chars.add(element.toString())
    }
    val res = chars.filter { it.matches(Regex("""\d""")) }
    return when {
        phone.contains(Regex("""[^\s\-^+\d()]|(\(\D*\))""")) -> ""
        phone.contains(Regex("""^\+""")) -> res.joinToString(prefix = "+", separator = "")
        else -> res.joinToString(separator = "")
    }
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.contains(Regex("""[^%\-\s\d]"""))) return -1
    val parts = jumps.split(" ")
    val res = parts.filter { it.contains(Regex("""^\d+$""")) }.map { it.toInt() }
    return res.max() ?: -1
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (!jumps.contains(Regex("""^((\d+\s([+\-%])+)\s?)+$"""))) return -1
    val parts = jumps.split(" +")
    val res = mutableListOf<Int>()
    if (parts[0].contains(Regex("""^\d+$"""))) res.add(parts[0].toInt())
    for (part in parts) {
        val matchResult = ((Regex("""\D*\s(\d+)$""").find(part)) ?: continue).groupValues.drop(1)
        res.add(matchResult[0].toInt())
    }
    return res.max()!!
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (!expression.contains(Regex("""^((\d+)\s+([-+])\s+)*(\d+)$"""))) throw IllegalArgumentException()
    val parts = expression.split(" ")
    var res = 0
    var k = 1
    for (i in parts.indices) {
        when {
            i % 2 == 0 -> {
                if (parts[i].contains(Regex("""^\d+$""")))
                    res += parts[i].toInt() * k
                else throw IllegalArgumentException()
            }
            (parts[i] == "+") && (i % 2 != 0) -> k = 1
            (parts[i] == "-") && (i % 2 != 0) -> k = -1
        }

    }
    return res
}


/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val words = str.split(" ")
    for (i in 0..words.size - 2) {
        if (words[i].toLowerCase() == words[i + 1].toLowerCase()) {
            val word1 = words[i]
            val word2 = words[i + 1]
            val x = str.split("$word1 $word2")
            return x[0].length
        }
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше либо равны нуля.
 */
fun mostExpensive(description: String): String = TODO()

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int = TODO()

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeCommandsInLoop(commands: String): String {
    var countBrace = 0
    val res = StringBuilder()
    for (char in commands) {
        when {
            char.toString() == "]" -> countBrace -= 1
            char.toString() == "[" -> countBrace += 1
        }
        res.append(char)
        if (countBrace == 0) break
    }
    res.deleteCharAt(0)
    return res.toString()
}

fun computeConveyor(list: List<Int>, commandsIn: String): List<Int> {
    var res = list.toMutableList()
    res.removeAt(res.size - 1)
    res.removeAt(res.size - 1)
    var ttl = list[list.size - 2]
    var sensor = list.last()
    var commandsInLoop = StringBuilder()
    var countBrace = 0
    val commands = StringBuilder(commandsIn)

    do {
        if (sensor !in 0 until res.size) throw IllegalStateException()
        if (commands.isEmpty()) break

        when (commands.first().toString()) {
            ">" -> sensor += 1
            "<" -> sensor -= 1
            "+" -> res[sensor] += 1
            "-" -> res[sensor] -= 1
        }
        if (ttl == 1) break
        if (commands.first().toString() == "[") {
            countBrace += 1
            val inLoop = computeCommandsInLoop(commands.toString())

            commandsInLoop = StringBuilder(inLoop)
            commandsInLoop.deleteCharAt(commandsInLoop.length - 1)
            if (res[sensor] == 0) {

                countBrace -= 1
                val numberToDelete = inLoop.length
                commands.delete(0, numberToDelete)
            } else {
                res.add(ttl - 1)
                res.add(sensor)

                res = computeConveyor(res, commandsInLoop.toString()).toMutableList()

                sensor = res.last()
                res.removeAt(res.size - 1)

                ttl = res.last() + 1
                res.removeAt(res.size - 1)

                val numberToDelete = commandsInLoop.length
                commands.delete(0, numberToDelete)
            }
        } else
            if (commands.first().toString() == "]") {
                countBrace -= 1
                if (countBrace < 0) throw IllegalArgumentException()
                if (res[sensor] != 0) {

                    res.add(ttl - 1)
                    res.add(sensor)

                    res = computeConveyor(res, commandsInLoop.toString()).toMutableList()

                    sensor = res.last()
                    res.removeAt(res.size - 1)

                    ttl = res.last() + 1
                    res.removeAt(res.size - 1)

                    commands.deleteCharAt(0)
                    if (res[sensor] != 0) {
                        commands.insert(0, "[$commandsInLoop]")
                    }
                    commands.insert(0, "]")

                }

            }

        commands.deleteCharAt(0)
        ttl -= 1
        if (sensor !in 0 until res.size) throw IllegalStateException()
        if (commands.isEmpty()) break
    } while (ttl > 0)


    res.add(ttl)
    res.add(sensor)
    return res
}

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    if (commands.contains(Regex("""[^><\-+\[\]\s]"""))) throw IllegalArgumentException()

    var countBrace = 0
    val matches = Regex("""[\[\]]""").findAll(commands)
    val z = matches.map { it.value }.toList()
    for (part in z) {
        if (part == "]") countBrace -= 1
        else countBrace += 1
        if (countBrace < 0) throw IllegalArgumentException()
    }
    if (countBrace != 0) throw IllegalArgumentException()


    val res = mutableListOf<Int>()
    for (i in 0 until cells) {
        res.add(0)
    }

    val sensor = cells / 2
    res.add(limit)
    res.add(sensor)
    val result = computeConveyor(res, commands).toMutableList()
    result.removeAt(result.size - 1)
    result.removeAt(result.size - 1)
    return result
}


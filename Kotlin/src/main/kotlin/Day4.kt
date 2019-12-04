class Day4(): Day<Int, Int> {
    override fun part1(): Int {
        val from = 134564
        val to = 585159
        
        return (from .. to).map { isValid(it.toString()) }.count { it.first }
    }

    override fun part2(): Int {
        val from = 134564
        val to = 585159

        return (from .. to).map { isValid(it.toString()) }.count { it.first && it.second.contains(2) }
    }

    fun isValid(string: String): Pair<Boolean, List<Int>> {
        var prec = string[0].toInt()
        var hasDouble = false
        var wasDouble = false
        val doubles = emptyList<Int>().toMutableList()

        for (c in string.drop(1)) {
            var current = c.toInt()
            if (current < prec) {
                return Pair(false, emptyList());
            }
            if (current == prec) {
                hasDouble = true
                 if (!wasDouble) {
                    doubles.add(doubles.lastIndex + 1, 2)
                    wasDouble = true;
                } else {
                    doubles.set(doubles.lastIndex, doubles[doubles.lastIndex] + 1)
                }
            } else {
                wasDouble = false
            }
            prec = current
        }

        return Pair(hasDouble, doubles);
    }
}
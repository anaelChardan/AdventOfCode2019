class IntCodeComputer(val instructions: List<Int>) {
    fun executeInstruction(result: Result): Result {
        val emptyInstruction = "00000";
        val currentInstruction: String = result.values[result.nextPosition].toString()
        val realInstruction: String = emptyInstruction.drop(currentInstruction.length) + currentInstruction
        val opcode = realInstruction.drop(3)
        val firstParamMode = realInstruction[2].toParameterMode()
        val secondParamMode = realInstruction[1].toParameterMode()
        val thirdParamMode = realInstruction[0].toParameterMode()

        return when (opcode) {
            "99" -> Halt(12).compute(result)
            "01" -> Addition(
                Parameter(firstParamMode, result.values[result.nextPosition + 1]),
                Parameter(secondParamMode, result.values[result.nextPosition + 2]),
                Parameter(thirdParamMode, result.values[result.nextPosition + 3])
            ).compute(result)
            "02" -> Times(
                Parameter(firstParamMode, result.values[result.nextPosition + 1]),
                Parameter(secondParamMode, result.values[result.nextPosition + 2]),
                Parameter(thirdParamMode, result.values[result.nextPosition + 3])
            ).compute(result)
            "03" -> AskInput(Parameter(firstParamMode, result.values[result.nextPosition + 1])).compute(result)
            "04" -> Output(Parameter(firstParamMode, result.values[result.nextPosition + 1])).compute(result)
            "05" -> JumpIfTrue(Parameter(firstParamMode, result.values[result.nextPosition + 1]), Parameter(secondParamMode, result.values[result.nextPosition + 2])).compute(result)
            "06" -> JumpIfFalse(Parameter(firstParamMode, result.values[result.nextPosition + 1]), Parameter(secondParamMode, result.values[result.nextPosition + 2])).compute(result)
            "07" -> IsLessThan(
                Parameter(firstParamMode, result.values[result.nextPosition + 1]),
                Parameter(secondParamMode, result.values[result.nextPosition + 2]),
                Parameter(thirdParamMode, result.values[result.nextPosition + 3])
            ).compute(result)
            "08" -> Equals(
                Parameter(firstParamMode, result.values[result.nextPosition + 1]),
                Parameter(secondParamMode, result.values[result.nextPosition + 2]),
                Parameter(thirdParamMode, result.values[result.nextPosition + 3])
            ).compute(result)
            else -> error("Unkown opcode $opcode")
        }
    }

    public fun run(input: List<Int>): Result
    {
        return generateSequence(Result(instructions, 0, false, input, emptyList()), ::executeInstruction).takeWhile { !it.needsHalt }.last()
    }
}

enum class PARAMETER_MODE { POSITION_MODE, IMMEDIATE_MODE }

fun Char.toParameterMode() : PARAMETER_MODE = if (this == '0') PARAMETER_MODE.POSITION_MODE else PARAMETER_MODE.IMMEDIATE_MODE

data class Parameter(val parameterMode: PARAMETER_MODE, val value: Int) {
    fun getValue(values: List<Int>): Int = when(parameterMode) {
        PARAMETER_MODE.POSITION_MODE -> values[value]
        PARAMETER_MODE.IMMEDIATE_MODE -> value
    }
}

data class Result(val values: List<Int>, val nextPosition: Int, val needsHalt: Boolean, val input: List<Int>, val output: List<Int>)

interface Instruction {
    fun compute(result: Result): Result
}

data class Addition(val firstParam: Parameter, val secondParam: Parameter, val location: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val listToUpdate = result.values.toMutableList()
        listToUpdate[location.value] = firstParam.getValue(result.values) + secondParam.getValue(result.values)

        return result.copy(values = listToUpdate.toList(), nextPosition = result.nextPosition + 4)
    }
}
data class Times(val firstParam: Parameter, val secondParam: Parameter, val location: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val listToUpdate = result.values.toMutableList()
        listToUpdate[location.value] = firstParam.getValue(result.values) * secondParam.getValue(result.values)

        return result.copy(values = listToUpdate.toList(), nextPosition = result.nextPosition + 4)
    }
}

data class Halt(val random: Int): Instruction {
    override fun compute(result: Result): Result {
        return result.copy(nextPosition = result.nextPosition + 1, needsHalt = true)
    }
}

data class AskInput(val storeAt: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val listToUpdate = result.values.toMutableList()
        listToUpdate[storeAt.value] = result.input.first()

        return result.copy(values = listToUpdate.toList(), nextPosition = result.nextPosition + 2, input = result.input.drop(1))
    }
}

data class Output(val toOutput: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val valueToDisplay = toOutput.getValue(result.values)

        return result.copy(nextPosition = result.nextPosition + 2, output = result.output.plus(valueToDisplay))
    }
}

data class JumpIfTrue(val firstParam: Parameter, val secondParam: Parameter): Instruction {
    override fun compute(result: Result): Result {
        if (firstParam.getValue(result.values) != 0) {
            return result.copy(nextPosition = secondParam.getValue(result.values))
        }

        return result.copy(nextPosition = result.nextPosition + 3)
    }
}

data class JumpIfFalse(val firstParam: Parameter, val secondParam: Parameter): Instruction {
    override fun compute(result: Result): Result {
        if (firstParam.getValue(result.values) == 0) {
            return result.copy(nextPosition = secondParam.getValue(result.values))
        }

        return result.copy(nextPosition = result.nextPosition + 3)
    }
}

data class IsLessThan(val firstParam: Parameter, val secondParam: Parameter, val location: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val listToUpdate = result.values.toMutableList()
        listToUpdate[location.value] = if (firstParam.getValue(result.values) < secondParam.getValue(result.values)) 1 else 0

        return result.copy(values = listToUpdate.toList(), nextPosition = result.nextPosition + 4)
    }
}

data class Equals(val firstParam: Parameter, val secondParam: Parameter, val location: Parameter): Instruction {
    override fun compute(result: Result): Result {
        val listToUpdate = result.values.toMutableList()
        listToUpdate[location.value] = if (firstParam.getValue(result.values) == secondParam.getValue(result.values)) 1 else 0

        return result.copy(values = listToUpdate.toList(), nextPosition = result.nextPosition + 4)
    }
}
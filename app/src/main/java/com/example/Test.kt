package com.example

class Test {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
//            val controller = TvController()
//            controller.powerOn()
//            controller.nextChannel()
//            controller.turnDown()
//            controller.powerOFF()
//            controller.preChannel()
//            controller.turnUp()

            val controller = NewTvController2()
            controller.powerOn()
            controller.nextChannel()
            controller.turnDown()
            controller.powerOff()
            controller.preChannel()
            controller.turnUp()
        }
    }
}

const val POWER_ON = 1
const val POWER_OFF = 0

//电视
class TvController {
    //状态
    var mState = POWER_OFF

    fun powerOn() {
        if (mState == POWER_OFF) {
            println("开机了")
        }
        mState = POWER_ON
    }

    fun powerOFF() {
        if (mState == POWER_ON) {
            println("关机了")
        }
        mState = POWER_OFF
    }

    fun nextChannel() {
        if (mState == POWER_ON) {
            println("下一个频道")
        } else {
            println("关机状态，无法调下一个频道")
        }
    }

    fun preChannel() {
        if (mState == POWER_ON) {
            println("上一个频道")
        } else {
            println("关机状态，无法调上一个频道")
        }
    }

    fun turnUp() {
        if (mState == POWER_ON) {
            println("调高音量")
        } else {
            println("关机状态，无法调高音量")
        }
    }

    fun turnDown() {
        if (mState == POWER_ON) {
            println("调低音量")
        } else {
            println("关机状态，无法调低音量")
        }
    }
}
/*-------------------------------------------------------------*/
interface ITvState {
    fun preChannel()
    fun nextChannel()
    fun turnUp()
    fun turnDown()
}

class OffState : ITvState {
    override fun preChannel() {
        println("关机状态，无法调上一个频道")
    }

    override fun nextChannel() {
        println("关机状态，无法调下一个频道")
    }

    override fun turnUp() {
        println("关机状态，无法调高音量")
    }

    override fun turnDown() {
        println("关机状态，无法调低音量")
    }
}

class OnState : ITvState {
    override fun preChannel() {
        println("上一个频道")
    }

    override fun nextChannel() {
        println("下一个频道")
    }

    override fun turnUp() {
        println("调高音量")
    }

    override fun turnDown() {
        println("调低音量")
    }
}

interface IPowerController {
    fun powerOn()
    fun powerOff()
}

class NewTvController : IPowerController {
    private lateinit var state: ITvState
    override fun powerOn() {
        state = OnState()
        println("开机了")
    }

    override fun powerOff() {
        state = OffState()
        println("关机了")
    }

    fun nextChannel() {
        state.nextChannel()
    }

    fun preChannel() {
        state.preChannel()
    }

    fun turnUp() {
        state.turnUp()
    }

    fun turnDown() {
        state.turnDown()
    }
}

class NewTvController2{
    private lateinit var state: ITvState
    fun powerOn() {
        state = OnState()
        println("开机了")
    }

    fun powerOff() {
        state = OffState()
        println("关机了")
    }

    fun nextChannel() {
        state.nextChannel()
    }

    fun preChannel() {
        state.preChannel()
    }

    fun turnUp() {
        state.turnUp()
    }

    fun turnDown() {
        state.turnDown()
    }
}
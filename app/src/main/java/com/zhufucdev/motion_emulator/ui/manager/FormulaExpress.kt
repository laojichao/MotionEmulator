package com.zhufucdev.motion_emulator.ui.manager

import com.zhufucdev.motion_emulator.R

enum class FormulaExpress(
    val isConstant: Boolean,
    val value: String,
    val label: Int
) {
    Sin(false, "sin", R.string.name_sin),
    Sinh(false, "sinh", R.string.name_sinh),
    Cos(false, "cos", R.string.name_cos),
    Cosh(false, "cosh", R.string.name_cosh),
    Tan(false, "tan", R.string.name_tan),
    Tanh(false, "tanh", R.string.name_tanh),
    Log(false, "log", R.string.name_log),
    Abs(false, "abs", R.string.name_abs),
    Sqrt(false, "sqrt", R.string.name_sqrt),
    Cbrt(false, "cbrt", R.string.name_cbrt),
    Round(false, "round", R.string.name_round),
    Floor(false, "floor", R.string.name_floor),
    Ceil(false, "ceil", R.string.name_ceil),
    Rand(false, "rand", R.string.name_rand),
    Pi(true, "pi", R.string.name_pi),
    E(true, "e", R.string.name_e);
}

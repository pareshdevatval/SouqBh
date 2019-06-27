package com.souqbh.utils.validator


/**
 * Created by Paresh Devatval
 */


/**
 * A data class which needs to be passed with error msg and error constant
 * from the presenter to the activity, whenever an error occurs.
 */
data class ValidationErrorModel(val msg: Int, val error: ValidationError)
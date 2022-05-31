package com.xinto.partialgen

/**
 * Generates a Partial subclass with all constructor parameters
 * boxed in [PartialValue] and marked [PartialValue.Missing] by default.
 */
@Target(AnnotationTarget.CLASS)
public annotation class Partialable

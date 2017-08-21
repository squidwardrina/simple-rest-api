package utils

/** Represents a char inside the mask. It can be letter or digit. */
object MaskChar extends Enumeration {
  type MaskChar = Value
  val letter, digit = Value
}
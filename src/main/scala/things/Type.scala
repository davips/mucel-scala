package things

sealed trait Type

case class Wire() extends Type

case class Motor() extends Type

case class Sensor() extends Type

case class Isolant() extends Type
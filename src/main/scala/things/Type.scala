package things

sealed trait Type

case class Wire() extends Type

case class Motor() extends Type

case class Sensor() extends Type

case class Isolant() extends Type

case class Bulb(permanent: Boolean = true) extends Type

case class Egg() extends Type

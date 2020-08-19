package bb.expstyla.exp

final case class TypeException(private val message: String = "",
                               private val cause: Throwable = None.orNull)
                      extends Exception(message, cause) 
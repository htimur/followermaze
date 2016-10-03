package de.khamrakulov.followermaze

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
package object event {
  private[event] type LazyBool = () => Boolean
  private[event] val LazyTrue: LazyBool = () => true
}

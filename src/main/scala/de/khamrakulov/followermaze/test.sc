import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

val Array(a, b) = "a b".split(" ")

val pattern: Regex = "^([0-9]+)\\|F\\|([0-9]+)\\|([0-9]+)$".r

pattern.unapplySeq("444|F|60|50")

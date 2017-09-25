package org.intellij.scala.bundle

import org.intellij.scala.bundle.Mapper.{Mapper, _}

/**
  * @author Pavel Fatin
  */
object Descriptor {
  type Descriptor = PartialFunction[Component, Mapper]

  implicit class DescriptorExt(val descriptor: Descriptor) extends AnyVal {
    def |(other: Descriptor): Descriptor = {
      case component if descriptor.isDefinedAt(component) && other.isDefinedAt(component) => descriptor(component) | other(component)
      case component if descriptor.isDefinedAt(component) => descriptor(component)
      case component if other.isDefinedAt(component) => other(component)
    }

    def &(other: Descriptor): Descriptor = {
      case component if descriptor.isDefinedAt(component) && other.isDefinedAt(component) => descriptor(component) & other(component)
    }
  }
}

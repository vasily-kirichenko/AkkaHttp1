package contracts

final case class FormatType
  (id: Short,
   name: String,
   isCategorized: Boolean,
   isSafe: Boolean,
   isUnknown: Boolean,
   isObsolete: Boolean,
   idElf: Short)

object FormatTypeOps {
  type Tuple = (Short, String, Boolean, Boolean, Boolean, Boolean, Short)
}
package nextgen.lambda.ui.forms;

final class RowSpan {
   final Integer colSpan;
   final Integer rowSpan;

   RowSpan() {
      this(1, 1);
   }

   RowSpan(Integer colSpan, Integer rowSpan) {
      this.colSpan = colSpan;
      this.rowSpan = rowSpan;
   }

   @Override
   public String toString() {
      return colSpan + " " + rowSpan;
   }
}

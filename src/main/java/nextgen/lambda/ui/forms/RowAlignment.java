package nextgen.lambda.ui.forms;

final class RowAlignment {
   final String colAlign;
   final String rowAlign;

   RowAlignment() {
      this("FILL", "FILL");
   }

   RowAlignment(String colAlign, String rowAlign) {
      this.colAlign = colAlign;
      this.rowAlign = rowAlign;
   }

   @Override
   public String toString() {
      return colAlign + " " + rowAlign;
   }
}

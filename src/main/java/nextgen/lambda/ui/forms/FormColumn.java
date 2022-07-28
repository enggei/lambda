package nextgen.lambda.ui.forms;

public class FormColumn {

   private final nextgen.lambda.ui.forms.Form builder;
   private String alignment = "LEFT";
   private String size = "PREF";
   private String behaviour = "NONE";

   public FormColumn(nextgen.lambda.ui.forms.Form builder) {
      this.builder = builder;
   }

   public nextgen.lambda.ui.forms.Form builder() {
      return this.builder;
   }

   public String spec() {
      return alignment + ":" + size + ":" + behaviour;
   }

   public nextgen.lambda.ui.forms.FormColumn left() {
      this.alignment = "LEFT";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn center() {
      this.alignment = "CENTER";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn right() {
      this.alignment = "RIGHT";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn pref() {
      this.size = "PREF";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn none() {
      this.behaviour = "NONE";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn grow() {
      this.behaviour = "GROW";
      return this;
   }

   public nextgen.lambda.ui.forms.FormColumn newColumn() {
      return this.builder().newColumn();
   }

   public nextgen.lambda.ui.forms.FormRow newRow() {
      return this.builder.newRow();
   }

   public FormColumn dlu(int dlu) {
      this.size = dlu + "dlu";
      return this;
   }
}

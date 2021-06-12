/*    */ package safekeeper.gui.layout;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.FlowLayout;
/*    */ import javax.swing.BoxLayout;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JPanel;
/*    */ import safekeeper.gui.util.GUIUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RowsLayout
/*    */   extends BoxLayout
/*    */ {
/*    */   private final Container host;
/*    */   private final int hgap;
/*    */   private final int vgap;
/*    */   
/*    */   public RowsLayout(Container paramContainer) {
/* 36 */     this(paramContainer, 0, 0);
/*    */   }
/*    */   
/*    */   public RowsLayout(Container paramContainer, int paramInt1, int paramInt2) {
/* 40 */     super(paramContainer, 1);
/* 41 */     this.host = paramContainer;
/* 42 */     this.hgap = paramInt1;
/* 43 */     this.vgap = paramInt2;
/* 44 */     addVerticalSpacer();
/*    */   }
/*    */   
/*    */   private void addVerticalSpacer() {
/* 48 */     this.host.add(GUIUtils.makeVerticalStrut(this.vgap));
/*    */   }
/*    */   
/*    */   public void addRow(JComponent... paramVarArgs) {
/* 52 */     JPanel jPanel = new JPanel(new FlowLayout(1, this.hgap, 0));
/* 53 */     for (JComponent jComponent : paramVarArgs) jPanel.add(jComponent); 
/* 54 */     this.host.add(jPanel);
/* 55 */     addVerticalSpacer();
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\layout\RowsLayout.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
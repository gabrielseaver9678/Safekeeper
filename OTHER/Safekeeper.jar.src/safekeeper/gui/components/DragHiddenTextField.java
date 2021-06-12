/*    */ package safekeeper.gui.components;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.event.MouseAdapter;
/*    */ import java.awt.event.MouseEvent;
/*    */ import javax.swing.JTextField;
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
/*    */ 
/*    */ public class DragHiddenTextField
/*    */   extends JTextField
/*    */ {
/*    */   public DragHiddenTextField(final String password) {
/* 31 */     super(password, 1);
/*    */ 
/*    */ 
/*    */     
/* 35 */     setToolTipText("<html>Use with caution, as this will copy your password:<br/>Drag from the black box into another application to<br/>copy your password securely.</html>");
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     setDragEnabled(true);
/* 41 */     setForeground(Color.BLACK);
/* 42 */     setBackground(Color.BLACK);
/* 43 */     setSelectionColor(Color.BLACK);
/* 44 */     setSelectedTextColor(Color.BLACK);
/*    */     
/* 46 */     addMouseListener(new MouseAdapter()
/*    */         {
/*    */           public void mousePressed(MouseEvent param1MouseEvent) {
/* 49 */             DragHiddenTextField.this.setText(password);
/* 50 */             DragHiddenTextField.this.selectAll();
/* 51 */             DragHiddenTextField.this.getTransferHandler().exportAsDrag(DragHiddenTextField.this, param1MouseEvent, 1);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\components\DragHiddenTextField.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
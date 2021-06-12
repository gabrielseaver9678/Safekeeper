/*    */ package safekeeper.gui.layout;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.GroupLayout;
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
/*    */ public class MatchedPairsLayout
/*    */   extends GroupLayout
/*    */ {
/* 30 */   private final List<Component> leftComponents = new ArrayList<>(), rightComponents = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public MatchedPairsLayout(Container paramContainer) {
/* 35 */     super(paramContainer);
/*    */   }
/*    */   
/*    */   public void addMatch(Container paramContainer1, Container paramContainer2) {
/* 39 */     this.leftComponents.add(paramContainer1);
/* 40 */     this.rightComponents.add(paramContainer2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void finalizeLayout() {
/* 45 */     int i = this.leftComponents.size();
/*    */ 
/*    */     
/* 48 */     GroupLayout.SequentialGroup sequentialGroup1 = createSequentialGroup();
/*    */ 
/*    */     
/* 51 */     GroupLayout.ParallelGroup parallelGroup1 = createParallelGroup(GroupLayout.Alignment.TRAILING);
/* 52 */     for (Component component : this.leftComponents)
/* 53 */       parallelGroup1.addComponent(component); 
/* 54 */     sequentialGroup1.addGroup(parallelGroup1);
/*    */ 
/*    */     
/* 57 */     GroupLayout.ParallelGroup parallelGroup2 = createParallelGroup(GroupLayout.Alignment.LEADING);
/* 58 */     for (Component component : this.rightComponents)
/* 59 */       parallelGroup2.addComponent(component); 
/* 60 */     sequentialGroup1.addGroup(parallelGroup2);
/*    */     
/* 62 */     setHorizontalGroup(sequentialGroup1);
/*    */ 
/*    */ 
/*    */     
/* 66 */     GroupLayout.SequentialGroup sequentialGroup2 = createSequentialGroup();
/* 67 */     for (byte b = 0; b < i; b++)
/* 68 */       sequentialGroup2.addGroup(createParallelGroup(GroupLayout.Alignment.CENTER)
/* 69 */           .addComponent(this.leftComponents.get(b))
/* 70 */           .addComponent(this.rightComponents.get(b))); 
/* 71 */     setVerticalGroup(sequentialGroup2);
/*    */   }
/*    */ }


/* Location:              C:\all\projects\Safekeeper\classes\Safekeeper.jar!\safekeeper\gui\layout\MatchedPairsLayout.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */
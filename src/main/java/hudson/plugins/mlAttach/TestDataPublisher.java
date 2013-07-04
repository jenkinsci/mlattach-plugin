/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.mlAttach;



import hudson.XmlFile;
import hudson.model.AbstractBuild;
import hudson.tasks.test.TestObject;
import hudson.model.Descriptor;
import hudson.tasks.junit.CaseResult;
import hudson.tasks.junit.ClassResult;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;


import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author abourabee
 */
public class TestDataPublisher extends hudson.tasks.junit.TestDataPublisher {

    @org.kohsuke.stapler.DataBoundConstructor
    public TestDataPublisher() {}

    /**
     * Called after test results are collected by Hudson, to create a resolver for TestActions.
     */
    @Override
    public hudson.tasks.junit.TestResultAction.Data getTestData(
            AbstractBuild<?, ?> build,
            hudson.Launcher launcher,
            hudson.model.BuildListener listener,
            hudson.tasks.junit.TestResult testResult) throws
            java.io.IOException, InterruptedException {

        Data data = new Data(build);
        /*
        for(Action action : build.getActions())
            if(action instanceof TestAction){
                TestAction tAction = (TestAction) action;
                data.addAction(tAction.getTestObject().getId(), tAction);
        }*/
        return data;
    }
    
   public static class Data extends hudson.tasks.junit.TestResultAction.Data{
       private final AbstractBuild<?, ?> build;

       public Data(AbstractBuild<?, ?> build){
           this.build = build;
       }
      
      @Override
      public List<TestAction> getTestAction(hudson.tasks.junit.TestObject testObject){

          String id = testObject.getId();
          XmlFile data = new XmlFile(new File(build.getRootDir(), "mlattach/" + id+));
          if (data.exists()) {
              try {
                  TestAction r = (TestAction) data.read();
                  r.setOwner(this);
                  return Collections.singletonList(r);
              } catch (IOException e) {
                  // TODO: report and move on
              }
          }

         return Collections.emptyList();

//          TestAction result = tags.get(id);
//
//           if (testObject instanceof CaseResult) {
//               if(result != null){
//                   return Collections.<TestAction>singletonList(result);
//               }
//               CaseResult cr = (CaseResult) testObject;
//               //This conditonal eliminates the ones in the jelly
//               if (!cr.isPassed() && !cr.isSkipped()) {
//                   hudson.tasks.test.TestObject t = (hudson.tasks.test.TestObject) testObject;
//                   TestAction action = new TestAction(this,t);
//                   tags.put(t.getId(), action);
//                   return Collections.<TestAction>singletonList(action);
//               }
//           }


       }
      
      public void addAction(String testId, TestAction action) throws IOException {
          new XmlFile(new File(build.getRootDir(), "mlattach/" + testId+".xml")).write(action);
      }

      
    
  
   }
}

      
   
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
import  java.*;


import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author abourabee
 */
public class TestDataPublisher extends hudson.tasks.junit.TestDataPublisher {

    private String regex;

    @org.kohsuke.stapler.DataBoundConstructor
    public TestDataPublisher(String regex) { this.regex = regex;}


    public String getRegex(){
        return regex;
    }

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

          XmlFile data = new XmlFile(new File(build.getRootDir(), "mlattach/" + id+".xml"));
          if (data.exists()) {
              try {
                  TestAction r = (TestAction) data.read();
                  r.setOwner(this);
                  r.setTestObject((hudson.tasks.test.TestObject) testObject);

                  //Only display the form on pages with failed case results
                  if(testObject instanceof CaseResult){
                      CaseResult cr = (CaseResult) testObject;
                      if(!cr.isPassed() && !cr.isSkipped()){
                        return Collections.<TestAction>singletonList(r);
                      }
                  }

              } catch (IOException e) {
                  // TODO: report and move on
              }
          }
          return Collections.emptyList();
      }

      public void addAction(String testId, TestAction action) throws IOException {
          new XmlFile(new File(build.getRootDir(), "mlattach/" + testId+".xml")).write(action);
      }
    }

       @hudson.Extension
       public static class DescriptorImpl extends Descriptor<hudson.tasks.junit.TestDataPublisher>{

           @Override
           public String getDisplayName(){
               return "Tag test results";

           }
    
  
             }
}

      
   
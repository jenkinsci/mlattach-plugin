/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.mlAttach;

import java.io.*;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.plugins.mlAttach.TestDataPublisher.Data;
import java.util.Scanner;
import javax.servlet.ServletException;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 *
 * @author abourabee
 */
@ExportedBean(defaultVisibility =2 )
public class TestAction extends AbstractTestAction<Data> {

    protected hudson.tasks.test.TestObject testObject;

    TestAction(Data o, hudson.tasks.test.TestObject t) {
        super(o);
        this.testObject = t;
    }
    public void setTestObject(hudson.tasks.test.TestObject t){
        this.testObject = t;
    }
    
     public void doWrite(StaplerRequest request, StaplerResponse response)
    throws ServletException, IOException, FileNotFoundException, UnsupportedEncodingException{

        //Get the parameters from the form
        boolean type = request.getSubmittedForm().getBoolean("type");
         String id = request.getSubmittedForm().getString("id");

         //Set the error type and save it to the xml file
         this.setErrorType(type);
         owner.addAction(id,this);

        response.forwardToPreviousPage(request);
        
     }

    
    public hudson.tasks.test.TestObject getTestObject(){
        return testObject;
    }
   
    
    /**
     * This is the icon that is displayed in the sidebar.
     * This plugin does not currently use the sidebar.
     */
    @Override
    public String getIconFileName() {
        return null; 
    }

    /**
     * This is the text that is displayed in the sidebar.
     * This plugin does not currently use the sidebar.
     */
    @Override
    public String getDisplayName() {
        return null; 
    }

    @Override
    public String getUrlName() {
        return "mlAttach";
    }
    

    /**
     * This method is apparently for annotating the the actual
     * "Error Message", "Stack Trace", "Standard Output", etc.
     * raw text. This is not currently necessary.
     */
    @Override
    public String annotate(String text) {
        return text;
    }
}
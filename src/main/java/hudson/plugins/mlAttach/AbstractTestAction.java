/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hudson.plugins.mlAttach;

import java.io.*;
import org.kohsuke.stapler.*;
import org.kohsuke.stapler.export.ExportedBean;
import javax.servlet.*;
import hudson.model.*;
/**
 *
 * @author abourabee
 */
@ExportedBean(defaultVisibility =2 )
public abstract class AbstractTestAction<T> extends hudson.tasks.junit.TestAction {
    
    protected boolean error_set;
    protected boolean error_type;
    protected transient T owner;


    AbstractTestAction(T owner){
        this.owner = owner;
        this.error_set = false;
    }
    
    public void setOwner(T owner) {
        this.owner = owner;
    }


    public void setErrorType(boolean type){
        error_set = true;
        error_type = type;
 
    }
    
    
    public String getErrorType(){
       if(!error_set) return null;
       else if(error_type) return "Product Bug";
       return "Script Bug";
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
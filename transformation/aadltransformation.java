package edu.buaa.rse.aadltransformation;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import edu.buaa.rse.dotx.model.Component;
import edu.buaa.rse.dotx.model.Connection;
import edu.buaa.rse.dotx.model.Feature;
import edu.buaa.rse.dotx.model.SystemModel;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.DirectionType;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.ConnectionInstance;
import org.osate.aadl2.instance.ConnectionInstanceEnd;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.aadl2.instance.SystemInstance;
import org.osate.ui.actions.AaxlModifyActionAsJob;
import org.osate.xtext.aadl2.properties.util.PropertyUtils;

public class DoTransformation
  extends AaxlModifyActionAsJob
{
  public static String INSTNAME;
  public String workSpace;
  private URI testURI;
  
  protected String getActionName()
  {
    return "AADL2GSPN Transformation for dependability analysis";
  }
  
  protected void doAaxlAction(IProgressMonitor monitor, org.osate.aadl2.Element element)
  {
    System.out.println("Begin!!");
    
    org.osate.aadl2.Property reliability = lookupPropertyDefinition("Architecture_Analysis", "Reliability");
    org.osate.aadl2.Property pnt = lookupPropertyDefinition("Architecture_Analysis", "InitialNode");
    org.osate.aadl2.Property runTime = lookupPropertyDefinition("Architecture_Analysis", "RunTime");
    org.osate.aadl2.Property connectionTime = lookupPropertyDefinition("Architecture_Analysis", "ConnectionTime");
    
    org.osate.aadl2.Property schedulingScheme = lookupPropertyDefinition("Architecture_Analysis", "ProcessorPolicy");
    org.osate.aadl2.Property schePartTimeChipSize = lookupPropertyDefinition("Architecture_Analysis", "PartTimeChipSize");
    org.osate.aadl2.Property TaskPeriod = lookupPropertyDefinition("Architecture_Analysis", "ProcessPeriod");
    org.osate.aadl2.Property TaskDeadlineTime = lookupPropertyDefinition("Architecture_Analysis", "ProcessDeadlineTime");
    
    int id = 0;
    
    SystemInstance si = ((InstanceObject)element).getSystemInstance();
    INSTNAME = si.getName();
    
    String modelURI = si.eResource().getURI().trimSegments(1).appendSegment(si.getName()).appendFileExtension("xml").toString();
    System.out.println("URI1: " + modelURI);
    
    String testString1 = si.eResource().getURI().trimSegments(2).toString();
    String testString2 = testString1.replaceFirst("platform:/resource/", "");
    System.out.println("testString2: " + testString2);
    
    String modelURI0 = si.eResource().getURI().trimSegments(0).appendSegment(si.getName()).appendFileExtension("xml").toString();
    System.out.println("URI0: " + modelURI0);
    String modelURI1 = si.eResource().getURI().trimSegments(2).appendSegment("model").appendFileExtension("xml").toString();
    System.out.println("URI0: " + modelURI1);
    
    this.workSpace = Platform.getLocation().toString();
    
    String uriString = modelURI.replaceFirst("platform:/resource", Platform.getLocation().toString());
    System.out.println("URI2: " + uriString);
    
    SystemModel system = new SystemModel("testModel");
    
    EList<ComponentInstance> allComponentsOfSystem = si.getComponentInstances();
    ArrayList<Component> componentList = new ArrayList();
    edu.buaa.rse.dotx.model.Property runtimeProperty;
    edu.buaa.rse.dotx.model.Property schedulingSchemeProperty;
    edu.buaa.rse.dotx.model.Property schePartTimeChipSizeProperty;
    for (Iterator<ComponentInstance> it = allComponentsOfSystem.iterator(); it.hasNext();)
    {
      ComponentInstance currentComponent = (ComponentInstance)it.next();
      
      ComponentCategory category = currentComponent.getCategory();
      System.out.println("former");
      Component component = new Component("");
      System.out.println("latter");
      
      component.setComponentId(id);
      id++;
      component.setComponentCategory(category.name());
      component.setName(currentComponent.getName());
      EList<FeatureInstance> featureInstances = currentComponent.getFeatureInstances();
      for (Iterator<FeatureInstance> featureIterator = featureInstances.iterator(); featureIterator.hasNext();)
      {
        FeatureInstance fromFeature = (FeatureInstance)featureIterator.next();
        Feature toFeature = new Feature(fromFeature.getName());
        
        System.out.println("fromFeatureName: " + fromFeature.getName());
        
        toFeature.setParent(component);
        component.addFeature(toFeature);
      }
      List<edu.buaa.rse.dotx.model.Property> properties = new ArrayList();
      
      String reliabilityValue = String.valueOf(PropertyUtils.getRealValue(currentComponent, reliability));
      edu.buaa.rse.dotx.model.Property reliabilityProperty = new edu.buaa.rse.dotx.model.Property("Reliability", reliabilityValue);
      component.addProperty(reliabilityProperty);
      
      String pntValue = String.valueOf(PropertyUtils.getIntegerValue(currentComponent, pnt));
      edu.buaa.rse.dotx.model.Property initialNodeProperty = new edu.buaa.rse.dotx.model.Property("InitialNode", pntValue);
      component.addProperty(initialNodeProperty);
      
      String runTimeValue = String.valueOf(PropertyUtils.getIntegerValue(currentComponent, runTime));
      runtimeProperty = new edu.buaa.rse.dotx.model.Property("RunTime", runTimeValue);
      component.addProperty(runtimeProperty);
      
      String schedulingSchemeValue = String.valueOf(PropertyUtils.getStringValue(currentComponent, schedulingScheme));
      schedulingSchemeProperty = new edu.buaa.rse.dotx.model.Property("ProcessorPolicy", schedulingSchemeValue);
      component.addProperty(schedulingSchemeProperty);
      
      String schePartTimeChipSizeValue = String.valueOf(PropertyUtils.getIntegerValue(currentComponent, schePartTimeChipSize));
      schePartTimeChipSizeProperty = new edu.buaa.rse.dotx.model.Property("PartTimeChipSize", schePartTimeChipSizeValue);
      component.addProperty(schePartTimeChipSizeProperty);
      
      String TaskPeriodValue = String.valueOf(PropertyUtils.getIntegerValue(currentComponent, TaskPeriod));
      edu.buaa.rse.dotx.model.Property taskPeriodpProperty = new edu.buaa.rse.dotx.model.Property("ProcessPeriod", TaskPeriodValue);
      component.addProperty(taskPeriodpProperty);
      
      String TaskDeadlineTimeValue = String.valueOf(PropertyUtils.getIntegerValue(currentComponent, TaskDeadlineTime));
      edu.buaa.rse.dotx.model.Property taskDeadlineTimeProperty = new edu.buaa.rse.dotx.model.Property("ProcessDeadlineTime", TaskDeadlineTimeValue);
      component.addProperty(taskDeadlineTimeProperty);
      
      componentList.add(component);
      
      system.addComponent(component);
    }
    System.out.println("");
    System.out.println("component.size: " + componentList.size());
    EList<ConnectionInstance> AllConnectionsOfSystem = si.getConnectionInstances();
    for (Iterator<ConnectionInstance> ConnectionIterator = AllConnectionsOfSystem.iterator(); ConnectionIterator.hasNext();)
    {
      ConnectionInstance fromConnection = (ConnectionInstance)ConnectionIterator.next();
      Connection toConnection = new Connection("");
      
      toConnection.setName(fromConnection.getName());
      try
      {
        List<InstanceObject> AllFeaturesOfConnection = fromConnection.getThroughFeatureInstances();
        for (Iterator featureIterator = AllFeaturesOfConnection.iterator(); featureIterator.hasNext();)
        {
          boolean flag = false;
          FeatureInstance fromFeature = (FeatureInstance)featureIterator.next();
          String fName = fromFeature.getName();
          String pName = fromFeature.getComponentInstance().getName();
          System.out.println("featureName: " + fName);
          System.out.println("parentName: " + pName);
          if (fromFeature.getDirection() == DirectionType.IN)
          {
            for (Component componentIN : componentList)
            {
              for (Feature feature : componentIN.features)
              {
                System.out.println("fname: " + feature.getName() + "   " + "pname: " + feature.getParent().getName());
                if ((pName == feature.getParent().getName()) && (fName == feature.getName()))
                {
                  toConnection.setDestination(feature);
                  System.out.println("IN----------------");
                  flag = true;
                  break;
                }
              }
              if (flag) {
                break;
              }
            }
          }
          else if (fromFeature.getDirection() == DirectionType.OUT)
          {
            for (Component componentOUT : componentList)
            {
              for (Feature feature : componentOUT.features)
              {
                System.out.println("fname: " + feature.getName() + "   " + "pname: " + feature.getParent().getName());
                if ((pName == feature.getParent().getName()) && (fName == feature.getName()))
                {
                  toConnection.setSource(feature);
                  System.out.println("OUT----------------");
                  flag = true;
                  break;
                }
              }
              if (flag) {
                break;
              }
            }
          }
          else if (fromFeature.getDirection() == DirectionType.IN_OUT)
          {
            String sourceFName = "";
            String destinationName = "";
            for (FeatureInstance sourceFeature : fromConnection.getSource().getComponentInstance().getFeatureInstances()) {
              if (sourceFeature.getName() == fName)
              {
                System.out.println("sourceName: " + fName);
                sourceFName = fName;
                break;
              }
            }
            for (FeatureInstance destinationFeature : fromConnection.getDestination().getComponentInstance().getFeatureInstances()) {
              if (destinationFeature.getName() == fName)
              {
                System.out.println("destinationNaem: " + fName);
                destinationName = fName;
                break;
              }
            }
            for (Component componentInOut : componentList)
            {
              for (Feature feature : componentInOut.features)
              {
                System.out.println("fname: " + feature.getName() + "   " + "pname: " + feature.getParent().getName());
                if ((pName == feature.getParent().getName()) && (sourceFName == feature.getName()))
                {
                  toConnection.setSource(feature);
                  flag = true;
                  break;
                }
                if ((pName == feature.getParent().getName()) && (destinationName == feature.getName()))
                {
                  toConnection.setDestination(feature);
                  flag = true;
                  break;
                }
              }
              if (flag) {
                break;
              }
            }
          }
          System.out.println("---------------------------------");
        }
      }
      catch (Exception e)
      {
        return;
      }
      String connectionTimeValue = String.valueOf(PropertyUtils.getIntegerValue(fromConnection, connectionTime));
      edu.buaa.rse.dotx.model.Property connectionTimepProperty = new edu.buaa.rse.dotx.model.Property("ConnectionTime", connectionTimeValue);
      toConnection.addProperty(connectionTimepProperty);
      
      system.addConnection(toConnection);
    }
    FileOutputStream fos = null;
    try
    {
      System.out.println("����������������:" + uriString);
      fos = new FileOutputStream(uriString);
      XStream x = new XStream(new DomDriver());
      XStream.setupDefaultSecurity(x);
      Class[] classes = { SystemModel.class, edu.buaa.rse.dotx.model.Property.class, Feature.class, edu.buaa.rse.dotx.model.Element.class, Connection.class, Component.class };
      x.allowTypes(classes);
      x.autodetectAnnotations(true);
      
      x.toXML(system, fos);
      fos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    System.out.println("It's over!");
    
    System.out.println(Platform.getLocation().toString());
    refresh("project", Platform.getLocation().toString(), testString2);
  }
  
  public static void refresh(String refreshType, String resourcePath, String projectName)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject project = root.getProject(projectName);
    if (project.exists()) {
      try
      {
        if ("project".equals(refreshType))
        {
          project.refreshLocal(2, null);
        }
        else if (("package".equals(refreshType)) || ("floder".equals(refreshType)))
        {
          IFolder floder = project.getFolder(resourcePath);
          if (floder.exists()) {
            floder.refreshLocal(2, null);
          }
        }
        else if (("packagefile".equals(refreshType)) || ("floderfile".equals(refreshType)) || 
          ("\tfile".equals(refreshType)))
        {
          IFile file = project.getFile(resourcePath);
          if (file.exists()) {
            file.refreshLocal(2, null);
          }
        }
      }
      catch (CoreException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}

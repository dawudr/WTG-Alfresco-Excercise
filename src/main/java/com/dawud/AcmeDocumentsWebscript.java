package com.dawud;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.*;

import java.io.Serializable;
import java.util.*;

public class AcmeDocumentsWebscript extends DeclarativeWebScript
{
    static Logger log = Logger.getLogger(AcmeDocumentsWebscript.class);
    private Repository repository;
    private NodeLocatorService nodeLocatorService;
    private NodeService nodeService;
    private FileFolderService fileFolderService;

    public static final String KEY_NAME = "name";
    public static final String KEY_NODEREF = "noderef";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CHILD_COUNT = "children";
    public static final String KEY_TYPE_VALUE_INVOICE = "invoice";
    public static final String KEY_TYPE_VALUE_DOCUMENT = "document";
    public static final String KEY_TYPE_VALUE_FOLDER = "folder";

    public static final QName CUSTOM_ASPECT_QNAME = QName.createQName("invoice.model", "Invoice");
    public static final QName PROP_QNAME_MY_PROPERTY = QName.createQName("invoice.model", "invoice");

    public void setRepository(Repository repository)
    {
        this.repository = repository;
    }

    public void setNodeLocatorService(NodeLocatorService nodeLocatorService) {
        this.nodeLocatorService = nodeLocatorService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    protected Map<String, Object> executeImpl(WebScriptRequest req,
                                              Status status, Cache cache) {

        NodeRef folder = getTopLevelFolder();
        log.info("Top level Folder Node Reference - " + folder.getStoreRef() + folder.getId());

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("folder", folder);
        model.put("folderTree", getFolderContents(folder));
        return model;
    }



    /**
     * Get nodeRef for top level AcmeDocuments
     * @return
     */
    public NodeRef getTopLevelFolder() {
        NodeRef folder;
        String folderPath = "Company Home/AcmeDocuments";
        String nodePath = "workspace/SpacesStore/" + folderPath;
        folder = repository.findNodeRef("path", nodePath.split("/"));

        // validate that folder has been found
        if (folder == null)
        {
            throw new WebScriptException(Status.STATUS_NOT_FOUND,
                    "Folder " + folderPath + " not found");
        }
        return folder;
    }


    /**
     * Get child nodes for Folder
     * @param folder
     * @return
     */
    public List<Map<String,String>> getFolderContents(NodeRef folder) {

        List<Map<String,String>> topLevelfilesList = new ArrayList<Map<String,String>>();
        // process parent
        topLevelfilesList.addAll(getDocuments(folder));

        // then folders
        Iterator<ChildAssociationRef> childAssocIt = nodeService.getChildAssocs(folder).iterator();
        while(childAssocIt.hasNext()) {
            NodeRef child = childAssocIt.next().getChildRef();
            //System.out.println(contentNode.getId() + " - " + nodeService.getProperty(contentNode, ContentModel.PROP_NAME)); // + " - " + nodeService.getProperties(contentNode));

            //System.out.println("FOLDER:" + contentNode.getId() + " - " + nodeService.getProperty(contentNode, ContentModel.PROP_NAME));
            // process folder
            if(!isDocument(child)) {
                String folderName = nodeService.getProperty(child, ContentModel.PROP_NAME).toString();
                Map<String, String> folderMap = new HashMap<String, String>();
                folderMap.put(KEY_NAME, folderName);
                folderMap.put(KEY_TYPE, KEY_TYPE_VALUE_FOLDER);
                folderMap.put(KEY_NODEREF, child.getId());
                folderMap.put(KEY_CHILD_COUNT, String.valueOf(nodeService.getChildAssocs(child).size()));
                topLevelfilesList.add(folderMap);
                topLevelfilesList.addAll(getDocuments(child));
            }
        }

        return topLevelfilesList;
    }

    /**
     * Get documents inside a folder
     * @param folder
     * @return
     */
    public List<Map<String,String>> getDocuments(NodeRef folder) {
        List<Map<String,String>> filesList = new ArrayList<Map<String, String>>();

        Iterator<ChildAssociationRef> childAssocIt = nodeService.getChildAssocs(folder).iterator();
        while(childAssocIt.hasNext()) {
            NodeRef child = childAssocIt.next().getChildRef();
            Map<String, String> fileMap = new HashMap<String, String>();
            if(isDocument(child)) {
                if (isInvoice(child)) {
                    log.debug("Found INVOICE:" + child.getId() + " - " + nodeService.getProperty(child, ContentModel.PROP_NAME));
                    fileMap.put(KEY_NAME, nodeService.getProperty(child, ContentModel.PROP_NAME).toString());
                    fileMap.put(KEY_TYPE, KEY_TYPE_VALUE_INVOICE);
                    fileMap.put(KEY_NODEREF, child.getId());
                } else {
                    log.debug("Found DOCUMENT:" + child.getId() + " - " + nodeService.getProperty(child, ContentModel.PROP_NAME));
                    fileMap.put(KEY_NAME, nodeService.getProperty(child, ContentModel.PROP_NAME).toString());
                    fileMap.put(KEY_TYPE, KEY_TYPE_VALUE_DOCUMENT);
                    fileMap.put(KEY_NODEREF, child.getId());
                }
                filesList.add(fileMap);
            }
        }
        return filesList;
    }


    /**
     * Test is a documents
     * @param document
     * @return
     */
    public boolean isDocument(NodeRef document) {
        return (!fileFolderService.getFileInfo(document).isFolder());
    }


    /**
     * Test invoice by looking for document and invoice string in preappended to filename
     * @param document
     * @return
     */
    public boolean isInvoice(NodeRef document) {
        return (nodeService.getProperty(document, ContentModel.PROP_NAME)).toString().toLowerCase().startsWith("invoice_");
    }

    /**
     * Add aspect
     * @param invoiceDocumentNode
     */
    public void addInvoiceAspect(NodeRef invoiceDocumentNode) {
        //add the metadata
        Map<QName,Serializable> aspectValues = new HashMap<QName,Serializable>();
        aspectValues.put(PROP_QNAME_MY_PROPERTY, "Amount");

        // Adding an aspect to a node
        nodeService.addAspect(invoiceDocumentNode, CUSTOM_ASPECT_QNAME, aspectValues);
    }
}

package com.dawud;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.model.Repository;
import org.alfresco.repo.security.authentication.AuthenticationComponent;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(RemoteTestRunner.class)
@Remote(runnerClass=SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class AcmeDocumentsWebscriptTest {

    private static final String ADMIN_USER_NAME = "admin";

    static Logger log = Logger.getLogger(AcmeDocumentsWebscriptTest.class);

    @Autowired
    protected AcmeDocumentsWebscript AcmeDocumentsWebscript;

    @Autowired
    @Qualifier("NodeService")
    protected NodeService nodeService;

    @Autowired
    private FileFolderService fileFolderService;

    @Autowired
    private Repository repository;

    // These NodeRefs are used by the test methods.
    private NodeRef invoiceDocumentNode, nonInvoiceDocumentNode, folderNode;

    @Before
    public void setUp() throws Exception {
        AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);

        // Create a map to contain the values of the properties of the node
        Map<QName, Serializable> invoiceDocumentprops = new HashMap<QName, Serializable>(1);
        Map<QName, Serializable> nonInvoiceDocumentprops = new HashMap<QName, Serializable>(1);
        Map<QName, Serializable> folderprops = new HashMap<QName, Serializable>(1);

        invoiceDocumentprops.put(ContentModel.PROP_NAME, "INVOICE_TEST.doc");
        nonInvoiceDocumentprops.put(ContentModel.PROP_NAME, "NORMALDOCUMENT_TEST.doc");
        folderprops.put(ContentModel.TYPE_FOLDER, "TESTFOLDER");
        folderprops.put(ContentModel.PROP_NAME, "TESTFOLDERNAME");
        folderprops.put(ContentModel.PROP_TITLE, "TESTFOLDERTITLE");
        folderprops.put(ContentModel.PROP_DESCRIPTION, "TESTFOLDERDESCRIPTION");


        // Create some test content
        invoiceDocumentNode = this.nodeService.createNode(
                AcmeDocumentsWebscript.getTopLevelFolder(),
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "Test Invoice Document"),
                ContentModel.TYPE_CONTENT,
                invoiceDocumentprops).getChildRef();

        nonInvoiceDocumentNode = this.nodeService.createNode(
                AcmeDocumentsWebscript.getTopLevelFolder(),
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "Test Normal Document"),
                ContentModel.TYPE_CONTENT,
                nonInvoiceDocumentprops).getChildRef();

        // Create a folder
        folderNode = this.nodeService.createNode(AcmeDocumentsWebscript.getTopLevelFolder(),
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,"TESTFOLDER"),
                ContentModel.TYPE_FOLDER,
                folderprops).getChildRef();
    }

    @Test
    public void testWiring() {
        assertNotNull(AcmeDocumentsWebscript);
    }

    @Test
    public void testGetTopLevelFolder() {
        AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
        NodeRef rootFolder = AcmeDocumentsWebscript.getTopLevelFolder();
        //System.out.println(rootFolder.getId());
        assertNotNull(rootFolder);
        String rootFolderName = (String) nodeService.getProperty(rootFolder, ContentModel.PROP_NAME);
        //System.out.println(rootFolderName);
        assertNotNull(rootFolderName);
        assertEquals("AcmeDocuments", rootFolderName);
    }

    @Test
    public void testGetFolderContents() {
        AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
        NodeRef rootFolder = AcmeDocumentsWebscript.getTopLevelFolder();
        List<Map<String,String>> documents = AcmeDocumentsWebscript.getFolderContents(rootFolder);

        Iterator<Map<String,String>> iterator = documents.iterator();
        while(iterator.hasNext()) {
            Map<String,String> content = iterator.next();

            assertNotNull(content);
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_TYPE));
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_NAME));
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_NODEREF));
            System.out.println(content);
        }
    }

    @Test
    public void testGetDocuments() {
        AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
        NodeRef rootFolder = AcmeDocumentsWebscript.getTopLevelFolder();
        List<Map<String,String>> documents = AcmeDocumentsWebscript.getDocuments(rootFolder);

        Iterator<Map<String,String>> iterator = documents.iterator();
        while(iterator.hasNext()) {
            Map<String,String> content = iterator.next();

            assertNotNull(content);
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_TYPE));
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_NAME));
            assertNotNull(content.get(AcmeDocumentsWebscript.KEY_NODEREF));
            //System.out.println(content);
        }
    }

    @Test
    public void testisDocument() {
        assertTrue(AcmeDocumentsWebscript.isDocument(invoiceDocumentNode));
        assertTrue(AcmeDocumentsWebscript.isDocument(nonInvoiceDocumentNode));
        assertFalse("Should be Folder!", AcmeDocumentsWebscript.isDocument(folderNode));

    }

    @Test
    public void testisInvoice() {
        assertTrue(AcmeDocumentsWebscript.isInvoice(invoiceDocumentNode));
        assertFalse("Not invoice!", AcmeDocumentsWebscript.isInvoice(nonInvoiceDocumentNode));
    }


    @Test
    public void testAddInvoiceAspect() {

        Map<QName, Serializable> invoiceDocumentAspectprops = new HashMap<QName, Serializable>(1);
        invoiceDocumentAspectprops.put(ContentModel.PROP_NAME, "INVOICE_ASPECT_TEST.doc");


        NodeRef invoiceDocumentAspectNode = this.nodeService.createNode(
                AcmeDocumentsWebscript.getTopLevelFolder(),
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, "Test Invoice Aspect Document"),
                ContentModel.TYPE_CONTENT,
                invoiceDocumentAspectprops).getChildRef();


        AcmeDocumentsWebscript.addInvoiceAspect(invoiceDocumentAspectNode);
        boolean hasAspect = nodeService.hasAspect(invoiceDocumentAspectNode, AcmeDocumentsWebscript.CUSTOM_ASPECT_QNAME);
        System.out.println(nodeService.getProperties(invoiceDocumentAspectNode));
        assertTrue("Must have aspect now", hasAspect);
    }


    @After
    public void tearDown() {
        nodeService.deleteNode(invoiceDocumentNode);
        nodeService.deleteNode(nonInvoiceDocumentNode);
        nodeService.deleteNode(folderNode);
    }




}

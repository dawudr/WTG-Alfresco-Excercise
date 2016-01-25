## Alfresco Answers

Please find my Alfresco answers. They were developed using AlfrescoSDK version 5.0.0 using Java 8. 

## Excercise 1 WebScript to list all documents in a folder

Here are the locations of the following files with a explanations

## Folder Structure

<pre>
├───src
│   ├───main
│   │   ├───amp
│   │   │   ├───config
│   │   │   │   └───alfresco
│   │   │   │       ├───extension
│   │   │   │       │   └───templates
│   │   │   │       │       └───webscripts - (1) listalldocuments webscript
│   │   │   │       └───module
│   │   │   │           └───repo-amp
│   │   │   │               ├───context - (2) webscript-content with webscript and java-backing bean registration
│   │   │   │               └───model (3) invoice-context with custom Aspect declaration
│   │   └───java
│   │       └───com
│   │           ├───dawud (4) Java code for listalldocuments webscript
│   └───test
│       ├───java
│       │   └───com
│       │       ├───dawud
│       │       └───empeccable (5) Unit test for listall document webscript
│       │           └───hubhqallinone
│       │               └───demoamp
│       │                   └───test
│       ├───properties
│       │   └───local
│       └───resources
│           └───alfresco
│               └───extension
└───target
</pre>

## Steps to create the listalldocuments webscript:
1) Declare the URL and properties of webscript

2) Register webscript and backing bean in the Spring context

3) Create and register the custom invoice Aspect

4) Java code for the webscripts in AcmeDocumentsWebscript.java

5) Unit tests used to build the webscript service in AcmeDocumentsWebscriptTest.java

6) Sample Test AcmeDocuments.zip contain an export of sample documents used to test the code and write the test cases with.

Also Nodeservice createNode method was used to create files and folders and tear down after the end of each unit test.


## Result

To call the webscript http://localhost:8080/alfresco/service/listalldocuments.json

This returned the following result given the Sample Test AcmeDocuments.zip uploaded into the repository.


{
"folder" :
{
"path" : "/Company Home",
"name" : "AcmeDocuments"
},
"children" : [
{

    name = invoice_January_001.pdf
    noderef = e1c18414-e1ee-4ff2-8936-e67a50fdf6ab
    type = invoice

{

    name = invoice_January_I200-109.png
    noderef = 75e5ab2b-1615-4ede-a5e8-16fb668fec49
    type = invoice

{

    name = logo-jhipster-drink-coffee.png
    noderef = 482bc564-a526-43f4-8459-92e1edd27f77
    type = document

{

    name = Advanced Testing and Debugging in AngularJS - yearofmoo.com.pdf
    noderef = 9a009b92-cd98-4344-b58f-c536dc47d479
    type = document

{

    name = INVOICE_ASPECT_TEST.doc
    noderef = e5ab692d-dc38-469c-8caa-e7bd127c9ea0
    type = invoice

{

    name = January
    noderef = da62fe15-6628-4e94-b390-64d84d419cae
    type = folder
    children = 3

{

    name = invoice_January_002.pdf
    noderef = ddcd7cd1-6d05-4d97-8cd6-5cfa49646a83
    type = invoice

{

    name = invoice_January_I200-109.png
    noderef = ec45a136-26ec-4ca8-bc62-596cd78c8aed
    type = invoice

{

    name = invoice_January_001.pdf
    noderef = 114a1efd-7035-45d1-a0a6-1f8d848716a3
    type = invoice

{

    name = February
    noderef = 79c5a41f-3ac0-4b1f-8f27-25a31f64715f
    type = folder
    children = 3

{

    name = invoice_February_001.pdf
    noderef = 1fba3891-a007-4516-a583-3e5b5aef8252
    type = invoice

{

    name = invoice_February_002.pdf
    noderef = 859eec1b-db39-4ec5-95ba-a7f1838c5c65
    type = invoice

{

    name = invoice_Febraury_I200-189.png
    noderef = 48ebf341-ae1a-4bee-a0fc-d1f4ccc7f58e
    type = invoice

{

    name = March
    noderef = 41da3dfd-a6b3-4978-bb5b-a3bd7c88624c
    type = folder
    children = 3

{

    name = Invoice_March_002.pdf
    noderef = 09c46f7e-c06a-41f0-bfc0-0adf665aabef
    type = invoice

{

    name = invoice_March_I200-109.png
    noderef = 16c0c8a8-d9a3-4ae9-8eba-ecea7c21dacd
    type = invoice

{

    name = Invoice_March_001.pdf
    noderef = 404d416a-2d58-4ecc-bfe9-a6bba0b215fa
    type = invoice

{

    name = April
    noderef = b2ee2f77-e2fc-44c6-8574-e69df87fd48a
    type = folder
    children = 3

{

    name = Invoice_April_002.pdf
    noderef = 22933558-284a-4198-bd7b-30a1e856961d
    type = invoice

{

    name = invoice_April_I200-189.png
    noderef = 633e2476-d24f-4623-8c97-efb0a5dcfda3
    type = invoice

{

    name = Invoice_April_001.pdf
    noderef = cfbd166b-ab7a-4dc1-831a-d661f6e9139d
    type = invoice

]
}


## Excercise 2 - Design an activiti workflow

I didn't get time to complete this excercise. 
My approach to this is to create the skeleton workflow with Alfresco share and then export this as xml. 
Copy these into the extension folder of the AlfrescoSDK repo amp. 
Then edit the XML in Eclipse using the Activi design modeller with conditions of the difficult customer.
And these rebuild the AMP war with the updated workflow.

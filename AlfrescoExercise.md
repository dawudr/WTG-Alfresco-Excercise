# Alfresco Exercise

Where possible answers should be provided in working java however for the sake of brevity in setup time, psuedo code can be used where necessary. This exercise is designed to be not too time consuming.

## Assumptions

* Alfresco CE 4+
* Java 1.7 / 8
* Using Spring 

### 1.  WebScript to list all documents in a folder

An audit is taking place of an alfresco folder and it's content store. Produce a webscript that lists all documents under the root folder `/AcmeDocuments/` across all folders, to can be called by an external program:

* Has a url of `/alfresco/s/listAllDocuments`
* Outputs a basic json array with noderef and children count `{ "name": "invoice_1234.pdf", "noderef": "1234ef-1234ef-1234ef", "children": 5 }`
* Any document that has a name prefixed `invoice_` requires the addition of an aspect "Invoice"
* Provide instructions of expected spring configuration
* Integration or Unit testing consideration or examples

### 2. Design an activiti workflow

Using this simple example: https://www.dropbox.com/s/t8rcxaof5rmarej/difficult_customer.png?dl=0

* Provide an example xml file to describe in BPMN the workflow provided.
* Provide explanation or code on how to make the 'complain' process a custom task listener


## Answers

Provide a public github url to the uploaded results, for prior review and discussion 24 hours before the planned interview.
Other things that we might advise candidates to be aware of for interview would be:

* CMIS Query Language
* Alfresco Custom Data Models

{
"folder" :
{
"path" : "${folder.displayPath}",
"name" : "${folder.name}"
},
"children" : [
<#list folderTree as child>
{

    <#list child?keys as key>
    ${key} = ${child[key]},
    </#list>

</#list>
]
}
# json-to-xml
Small application to convert data from JSON to XML format

##Example

###JSON:

```
{
   "menu":{
      "id":"file",
      "value":"File",
      "popup":{
         "menuitem":[
            {
               "value":"New",
               "onclick":"CreateNewDoc()"
            },
            {
               "value":"Open",
               "onclick":"OpenDoc()"
            },
            {
               "value":"Close",
               "onclick":"CloseDoc()"
            }
         ]
      }
   }
}
```
###XML:

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<OBJECT>
    <menu>
        <id>file</id>
        <value>File</value>
        <popup>
            <menuitem>
                <menuitem_ITEM>
                    <value>New</value>
                    <onclick>CreateNewDoc()</onclick>
                </menuitem_ITEM>
                <menuitem_ITEM>
                    <value>Open</value>
                    <onclick>OpenDoc()</onclick>
                </menuitem_ITEM>
                <menuitem_ITEM>
                    <value>Close</value>
                    <onclick>CloseDoc()</onclick>
                </menuitem_ITEM>
            </menuitem>
        </popup>
    </menu>
</OBJECT>
```

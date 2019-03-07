Credits
-------

### Contributors

*   Alan Banh (alan123banh)
*   Anthony Chu (anthony-chu)
*   Austin Chiang (austinchiang)
*   Jason Pince (jpince)
*   Victor Ware (vicnate5)

# Using PQL Queries in Liferay-Portal

This document shows how PQL queries can be defined and used in liferay-portal.

For information on Poshi Query Language (PQL) see [PQL documentation](pql.markdown)

## Defining PQL
In order to leverage PQL to create a subset of tests a 'test.batch.property.query[XXX]' property will need to be set within '[test.properties](https://github.com/liferay/liferay-portal/blob/master/test.properties)'.

See the below example:

```
test.batch.run.property.query[subrepository-functional-tomcat80-mysql56-jdk8]=\
		(portal.acceptance == "true") AND \
		(testray.main.component.name ~ "Web Content")
```

The above query will group all test commands (i.e. PortalSmoke#Smoke) that have the poshi properties of 'portal.acceptance' set to 'true', and have the 'testray.main.component.name' of 'Web Content Display' or 'Web Content Administration' into a list of test commands to be ran for a particular Jenkins job.

## Testing Queries
In order to test a query do the following within the root directory of a local 'liferay-portal' repository.

1. (Optional) Add the property 'test.batch.property.query' to a 'test.${COMPUTERNAME}.properties':

	```
	test.batch.property.query=\
		(portal.acceptance == "true") AND \
		(testray.main.component.name ~ "Web Content")
	test.batch.max.group.size=1
	```

1. Open a command line terminal and run the following command within the root directory of the 'liferay-portal' repository:

	```
	ant -f build-test.xml record-test-case-method-names
	```

	Alternatively pass in both properties from the command line, make sure to wrap the PQL property with single quote `'` and escape any `"`, `(`, `)`:

	```
	ant -f build-test.xml record-test-case-method-names -Dtest.batch.max.group.size=1 -Dtest.batch.property.query='\(portal.acceptance == \"true\"\) AND \(testray.main.component.name ~ \"Web Content\"\)'
	```

1. The result should look like this:

	```
     [exec] The following query returned 8 test class command names:
     [exec] ((portal.acceptance == "true") AND (testray.main.component.name ~ "Web Content")) AND (ignored != true) AND (test.run.environment == "CE" OR test.run.environment == null)
     [exec]
     [exec]
     [exec] BUILD SUCCESSFUL in 25s
     [exec] 1 actionable task: 1 executed
     [move] Moving 1 file to /opt/dev/projects/github/liferay-portal
     [echo]
     [echo] ##
     [echo] ## test.case.method.names.properties
     [echo] ##
     [echo]
     [echo]
     [echo] RUN_TEST_CASE_METHOD_GROUP_0=0_0 0_1 0_2 0_3 0_4 0_5 0_6 0_7
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_0=LocalFile.LocalizationWithWebContentUI#AddWCWithTranslation
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_1=LocalFile.WebContentTemplates#AddTemplateWithStructure
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_2=LocalFile.WebContent#EditWebContentViaArticleTitle
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_3=LocalFile.WebContentDisplay#AddAudioViaWebContent
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_4=LocalFile.WebContentDisplay#AddWebContent
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_5=LocalFile.WebContentDisplay#AddWithStructure
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_6=LocalFile.WebContentDisplay#RemoveWCDPortletSite
     [echo] RUN_TEST_CASE_METHOD_GROUP_0_7=LocalFile.WebContentDisplay#SelectWebContent
     [echo] RUN_TEST_CASE_METHOD_GROUPS=0
     [echo]
	```

	More queries may be appended as seen in the console output, this is normal behavior.

## Include Tests From Different Components

More complex queries can be used to include additional tests or get more specific.

Below is an example of a query used for a component that touches many other components:

```
test.batch.run.property.query[subrepository-functional-tomcat80-mysql56-jdk8]=\
	(portal.acceptance == "true") AND \
	(\
		(testray.component.names ~ "Dynamic Data Lists") OR \
		(testray.main.component.name ~ "Web Content") OR \
		(testray.main.component.name == "Dynamic Data Lists") OR \
		(testray.main.component.name == "Forms")\
	)
```

This query will include tests that set the property 'portal.acceptance' to 'true' and satisfies at least one of the conditions linked together by the 'OR' keyword.
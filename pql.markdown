Credits
-------

### Contributors

*   Alan Banh (alan123banh)
*   Anthony Chu (anthony-chu)
*   Austin Chiang (austinchiang)
*   Jason Pince (jpince)
*   Victor Ware (vicnate5)

# Poshi Query Language

Poshi Query Language (PQL) is a query language that is used to select Poshi tests. PQL is modeled on [JIRA Query Language (JQL)](https://confluence.atlassian.com/jirasoftwarecloud/advanced-searching-764478330.html).

## PQL Components
When 'poshi properties' are mentioned that is in reference to the properties set within the 'testcase' files.

A simple PQL query consists of a `field`, an `operator`, and a `value`.

A complex PQL query consists of two or more simple PQL linked together by the use of `keyword`.

Definition for `field`, `operator`, `value`, and `keyword` can be found below:

**Field**

The 'field' is representative of the poshi properties attributed to each individual 'testcase command'.

Here are some example properties that you can use:

* portal.acceptance
* testray.main.component.name
* testray.component.names
* ...

For a comprehensive list within 'portal' please see look for the property 'test.case.available.property.names' in [test.properties](https://github.com/liferay/liferay-portal/blob/master/test.properties).

**Operator**

The 'operator' is used to relate the 'field' value to the 'value' declared in the query statement.

Currently the available 'operators' are..

* `Equals (==) | Not Equals (!=)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' matches (does not match) the query 'value' specified that 'test command' will be added to the test set.
	* This 'operator' works with any value.
* `Contains (~) | Not Contains (!~)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' contains (does not contain) the query 'value' specified that 'test command' will be added to the test set.
	* This 'operator' only works with string values.
* `Greater Than (>) | Greater Than Or Equal To (>=)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' is greater than (or equal to) 'value' specified, then that 'test command' will be added to the test set.
	* This 'operator' only works with numeric values.
* `Less Than (<) | Less Than Or Equal To (<=)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' is less than (or equal to) 'value' specified, then that 'test command' will be added to the test set.
	* This 'operator' only works with numeric values.
If we need more **'operators'** please make a request to QA Engineering.

**Value**

The 'value' is representative of the value set within your 'test commands'. This will be used to compare within the query.

**Keyword**

The 'keyword' is used in a similar way that '[conditional operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)' are used within java. They allow for more complex queries to include or exclude a certain set of tests.

The currently existing keywords that we have are...

* AND
	* This will act as a logical 'and', meaning if you have 2 conditionals connected by an 'AND' both of those conditions must evaluate as 'true' in order for a test to be included within a set of tests.
* OR
	* This will act as a logical 'or', meaning if you have 2 conditionals connected by an 'OR' as long as one of those conditions have been evaluate as 'true' the test will be included within a set of tests.
* NOT
	* This will act as a logical 'not', meaning if you have a conditional preceded by a 'NOT' then it will flip the result. Basically, it will turn a 'true' to a 'false'.

If we need more 'keywords' please make a request to QA Engineering.

## Defining PQL
In order to leverage PQL to create a subset of tests you will need to set a 'test.batch.property.query[XXX]' within 'test.properties'.

See the below example:

```
test.batch.run.property.query[subrepository-functional-tomcat80-mysql56-jdk8]=\
		(portal.acceptance == "true") AND \
		(testray.main.component.name ~ "Web Content")
```

The above query will group all test commands (i.e. PortalSmoke#Smoke) that have the poshi properties of 'portal.acceptance' set to 'true', and have the 'testray.main.component.name' of 'Web Content Display' or 'Web Content Administration' into a list of test commands to be ran for a particular Jenkins job.

## Test Queries
In order to test a query do the following within your 'liferay-portal' repository.

1. (Optional) Add the property 'test.batch.property.query' to your 'test.${COMPUTERNAME}.properties':

	```
	test.batch.property.query=\
		(portal.acceptance == "true") AND \
		(testray.main.component.name ~ "Web Content")
	test.batch.max.group.size=1
	```

1. Open a command line and run the following command within your 'liferay-portal' repository:

	```
	ant -f build-test.xml record-test-case-method-names
	```

	Alternatively you can pass in both properties from the command line, make sure to wrap the PQL property with single quote `'` and escape any `"`, `(`, `)`:

	```
	ant -f build-test.xml record-test-case-method-names -Dtest.batch.max.group.size=1 -Dtest.batch.property.query='\(portal.acceptance == \"true\"\) AND \(testray.main.component.name ~ \"Web Content\"\)'
	```

1. This will give a result that looks something like this:

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

## Include even more tests

You can use even more complex queries to include additional tests or get more specific. Here is an example of a query used for a component that touches many other components:

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

Here, we are checking for sub-components(testray.component.names) so then we can include tests that are part of another main component without including all tests from that main component.
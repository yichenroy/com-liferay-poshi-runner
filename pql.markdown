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

Here are some example properties that can be used:

* portal.acceptance
* testray.main.component.name
* testray.component.names
* ...

For the most recent comprehensive list within 'liferay-portal' please see look for the property 'test.case.available.property.names' in [test.properties](https://github.com/liferay/liferay-portal/blob/master/test.properties).

**Operator**

The 'operator' is used to relate the 'field' value to the 'value' declared in the query statement.

Currently the available 'operators' are..

* `Equals (==) | Not Equals (!=)`
	* If a 'test command' has a 'poshi property name' that matches the query 'field', and 'poshi property value' matches(==) or does not match (!=) the query 'value' specified then that 'test command' will be added to the test set.
	* This 'operator' works with any value.
* `Contains (~) | Not Contains (!~)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' contains(~) or does not contain(!~) the query 'value' specified then that 'test command' will be added to the test set.
	* This 'operator' only works with string values.
* `Greater Than (>) | Greater Than Or Equal To (>=)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' is greater than (or equal to) 'value' specified, then that 'test command' will be added to the test set.
	* This 'operator' only works with numeric values.
* `Less Than (<) | Less Than Or Equal To (<=)`
	* If a 'test command' has a 'poshi property name' matches the query 'field', and 'poshi property value' is less than (or equal to) 'value' specified, then that 'test command' will be added to the test set.
	* This 'operator' only works with numeric values.
If There is a need for more **'operators'** please make a request to QA Engineering.

**Value**

The 'value' is representative of the value set within 'test commands'. This will be used to be compared when the query is executed.

**Keyword**

The 'keyword' is used in a similar way that '[conditional operators](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/op2.html)' are used within java. They allow for more complex queries to include or exclude a certain set of tests.

Currently there are three existing keywords:

* AND
	* This will act as a logical 'and', meaning the two conditionals connected by an 'AND' must both evaluate to 'true' in order for a test to be included within a set of tests.
* OR
	* This will act as a logical 'or', meaning at least one of the conditionals connected by an 'OR' must evaluate to 'true' for the test to be included within a set of tests.
* NOT
	* This will act as a logical 'not', meaning a conditional preceded by a 'NOT' will negate the result (i.e 'true' will evaluate to 'false' instead and vice versa).

If there is a need for more 'keywords' please make a request to QA Engineering.

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
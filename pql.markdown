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

## For examples on how PQL can be defined and used in liferay-portal, see [Using PQL Queries in Liferay-Portal documentation](using-pql-queries-in-liferay-portal.markdown)
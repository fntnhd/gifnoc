Story: Get some property values for some environments for an application

Given an application named: PropertyService with environments:

|environment |
|dev         |
|test        |
|prod        |

that has properties named:

|property    |
|service.url |
|db.url      |

with property values:

|environment |property name |property value      |
|dev         |service.url   |http://dev.service  |
|dev         |db.url        |http://dev.db       |
|test        |service.url   |http://test.service |
|test        |db.url        |http://test.db      |
|prod        |service.url   |http://prod.service |
|prod        |db.url        |http://prod.db      |

When I request the values of the following properties:

|application     |environment |property name |
|PropertyService |dev         |service.url   |
|PropertyService |dev         |db.url        |
|PropertyService |test        |service.url   |
|PropertyService |test        |db.url        |
|PropertyService |prod        |service.url   |
|PropertyService |prod        |db.url        |

Then the following property values are returned:

|property value      |
|http://dev.service  |
|http://dev.db       |
|http://test.service |
|http://test.db      |
|http://prod.service |
|http://prod.db      |

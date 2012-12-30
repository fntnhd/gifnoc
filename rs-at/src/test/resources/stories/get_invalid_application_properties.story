Story: Request undefined properties for an application

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

When I request the values of the following undefined properties:

|application     |environment |property name |
|PropertyService |dev         |service.db    |
|PropertyService |dev         |db.service    |
|PropertyService |test        |service.db    |
|PropertyService |test        |db.service    |
|PropertyService |prod        |service.db    |
|PropertyService |prod        |db.service    |
|OtherService    |dev         |service.url   |
|OtherService    |test        |service.url   |
|OtherService    |prod        |service.url   |
|PropertyService |perf        |service.url   |
|PropertyService |perf        |db.url        |

Then the following error messages are received:

|error message                                                                       |
|Invalid property [service.db] for application [PropertyService] environment [dev]   |
|Invalid property [db.service] for application [PropertyService] environment [dev]   |
|Invalid property [service.db] for application [PropertyService] environment [test]  |
|Invalid property [db.service] for application [PropertyService] environment [test]  |
|Invalid property [service.db] for application [PropertyService] environment [prod]  |
|Invalid property [db.service] for application [PropertyService] environment [prod]  |
|Invalid property [service.url] for application [OtherService] environment [dev]     |
|Invalid property [service.url] for application [OtherService] environment [test]    |
|Invalid property [service.url] for application [OtherService] environment [prod]    |
|Invalid property [service.url] for application [PropertyService] environment [perf] |
|Invalid property [db.url] for application [PropertyService] environment [perf]      |

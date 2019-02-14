# Hierarchy of the configuration

App --> Domains
Domains --> Intents & Entities
Intents --> Conversation Starting point
Conversation Defintions --> Data Objects
Data Objects --> Integration Defintion

# Other configurations

|Configuration|Purpose|
|--------|--------|
|Help|Conversation starting point for helps at domain, intent and conversation level|
|Menu|Menu definitions and starting conversation for menu| 
|Commands|Global intents which can be invoked by commands|
|DSV|Domain Specific Vocabulary Definitions|
||| 

# appList.json

```
This is used to define the application list. This app id gets updated as app in 
session.userData.currentApp. 

There is a provision to start a dialog for an application, when it is chosen. 

However, currently it is not used as a menu gets started when an application is chosen
```
|variable|Purpose|Sample Values|
|--------|-------|------------|
|id|unique id to identify the App|"Analytics"|
|description|This appears when you do **help apps**|"Interactive App to give insights about your Website's Analytics"|
|startDialog|Not used now as all app starts with menu|NA|
|entryPoint|First conversation json for startDialog. Not used now|NA|
|domains|An array containing the list of domains for the app|["GoogleAnalytics", "purpleSlate"]|
|nluType|Which NLU engine it uses|"RASA" or "LUIS"|
|nluUrl|where the NLU engine runs. This is in relation with what is defined in enviornment config file|google_analytics
||||

# domainList.json
|variable|Purpose|Sample Values|
|--------|-------|------------|
|id|unique id for the domains|"GoogleAnalytics"|
|description|Not used much. Mostly for reference|"Interactive App to give insights about your Website's Analytics"|
|menuId|Starting menu for the Domain|"GADomainMenu"|
||||

# domainList.json

|variable|Purpose|Sample Values|
|--------|-------|------------|
|id|unique id for the domains|"GoogleAnalytics"|
|description|Not used much. Mostly for reference|"Interactive App to give insights about your Website's Analytics"|
|menuId|Starting menu for the Domain|"GADomainMenu"|
||||

# intent-mapping.json

```
When the intent gets resolved by NLP engine, Bot should start the dialog. 
This configuration maps the intent to the dialog. 
All configurable dialogs should have botDialog as "/enterWaterFall" to denote Autowaterfall dialog. 
Entrypoint tells the first dialog (json) to pick. All jsons are kept in resources directory. Give the relative path from resources directory.  
```
|variable|Purpose|Member of|Sample Values|
|--------|-------|------------|--------|
|intent|Intent returned by NLP. Each intent mapping is an object|parent object|"usage"|
|botDialog|Dialog to start for the intent.|intent|"enterWaterFall"|
|entryPoint|Arguement to be passed for the start dialog.|intent|"GoogleAnalytics/ga_usage_1.json"|
||||

# intentDefinition.json

```
Ideally, this should be together with mapping. However, for registering dialogs mapping is kept separately. 

Intent definition configures everything about intent and entity. 
```

Entry point given intent mapping can be changed based on entities coming in and that definition is also kept here. 

Name of the intent would be used in affirmation. So, give meaningful name.

If an intent is business intent, it would be stored and used back, if the next intent is pronoun. For example, if you ask **what is the usage last month** it is a **usage** intent with *timeline* as *last month*. Now business intent is kept as **usage**. If user types again **how about last week**, now intent from RASA would be **pronoun**. Bot would use **usage** as intent now. Entity of *timeline* would be changed to *last week*

Though there is entrypoint for intent at intent mapping, there might be scenarios, we need to start dialog at different entry points based on entities coming in. For example, when an user asks for usage count, we may give an entry point to show usage data and then we may provide an option to break down by location or browser. If he chooses to breakdown by location, we may chain to next dialogue. But, it is possible for user to say **can you provide me the usage breakdown by location**. In which case, we may need to go directly to that dialogue. This is achieved by **entitiesPresent** and **entitiesMatch** properties in **mappings**


|variable|Purpose         |Member|Sample Values|psContext|
|-----|----------------|------|-----|-----|
|type|Intent returned by NLP.|parent object|"usage"|currentIntent|
|domain|Domain the intent belong to|parent object|"GoogleAnalytics"|domain|
|name|Name of the intent|parent|"Usage Count"|currentIntentName
|business|Is this a business intent|parent|true or false|-|
|affirmIntentOnLowConfidence|should there be a affirmation in case the intent score is lesser than the defined value in bot config|parent|true or false|-|
|entities|All possible entities for the intent|parent|-|currentEntities|
|type|entity name|entities|"timeline"|-|
|isMandatoryByUser|Not used now. Idea is to force the user to enter this|entities|true or false|-|
|default|Default value if user has not entered|entities|"yesterday"|-|
|defaultChatter|Not used now. Chatter to be shown for Default vale|entities|object|-|
|domain|Domain for Default Chatter|defaultChatter|"GoogleAnlytics"|-|
|type|Context for Default Chatter|defaultChatter|"AffirmIntent"|-|
|questionChatter|Not used now. Chatter to be shown for Mandatory entity|entities|object|-|
|domain|Domain for Question Chatter|questionChatter|"GoogleAnlytics"|-|
|type|Context for Question Chatter|questionChatter|"AskTimeline"|-|
|mappings|Array of Mapping for entrypoints|parent|object|-|
|entiesPresent|Array of entities to be present in the coming mesage to effect this mapping|mappings|["location", "browser"]|-|
|entryPoint|Starting json for the dialog|mappings|"GoogleAnalytics/usage_4.json"|-|
|entitiesMatch|Array of objects to match to effect his mapping|mappings|object|-|
|entityType|entity name to match the incoming entity|entitiesMatch|"location"|-|
|entityValue|entity value to match the incoming entity|entitiesMatch|"chennai"|-|
|||||


# Integration-mapping.json

```
Data given in conversation could be entities or could be psContext objects or could be data from interface systems. This Integration mapping defines which api to be called to get the object.

It can connect to only REST based APIs.

If the response contains error like SocketTimedOut and ConnectionError, then it would retry. 
```
|variable|Purpose|Member of|Sample Values|
|--------|------------------|----------|--------|
|objectReturned|Object returned by the API|parent|"usageData"|
|service|Interface system base URL. This refers to environment config|parent|"GA"|
|methodName|Http method|parent object|GET or POST|
|path|URL Path for the API.It can contain entity/psContext objects|parent|"/ga/usage"|
|inputParamMap|Array of Request Parameters for API|parent|-|
|type|Request paramater type|inputParamMap|"timeline"|
|value|Request parameter value. It can be object variable|inputParamMap|"yesterday" or can be ${psEntity.time}$|
|inputBodyMap|If request body need to be sent, configure here|parent|-|
|type|Body paramater type|inputBodyMap|"location"|
|value|Body parameter value. It can be object variable|inputParamMap|"chennai" or can be ${psEntity.location}$|
|noOfRetries|Maximum number of retries for retryable errot codes|parent|3|
|retryErrors|Array of error codes which can be retried|parent|["ETIMEDOUT", "ESOCKETTIMEDOUT"]|
|isReqBodyExist|Does Request needs body|parent|true or false|
|isReqParamsExist|Does Request params required|parent|true or false|
||||

# helpDialog-mapping.json

```
Helps are defined as conversations. 

This mapping defines entry points for each help

Help can be defined at three levels. Domain, intent and conversation. 

When a help command comes from user, bot looks at the current context. If the current context of the user is at the conversation, it looks for help mapping for the domain, intent and conversation, if it can not find anything, it falls back to domain and intent and check for any mapping, if it still can not find anything, it falls back to domain. If it can not find any mapping at domain level as well, it falls back to default help defined in the environment config (which is normally "purpleSlate")
```
On top of these three levels help, there are two more helps, which are **help commands** and **help apps**, which gives the description for commands and apps. 

Below definition is an array

|variable|Purpose        |Sample Values|
|--------|---------------|------------|
|domain|domain for which help is defined|"GoogleAnalytics"|
|intent|intent for which help is defined.Optional|"usage"|
|conversation|conversation json name. Full path not required. Optional|'ga_2.json'|
|botDialog|Dialog for help|'/enterWaterFall'|
|entryPoint|starting conversation for help|'purpleSlate/help/GoogleAnalytics_usage_ga_2_1.json'|
|||

# menuMap.json

```
Menu goes by the hierarchy of apps, domain and intent. However, the definition does not restrict that. A menu id, can lead to a sub menu items and that can lead to another menu. 

There is no separate menu definition for apps. When an app invoked all its domain's menu is consolidated and shown. 

When a menu item is chosen by the user, it can either have another menu or it can call the dialog for an intent. Whether it is menu or dialog is defined in the action. 

A Menu card always contains the app id as the title and the current menu description as subtitle. 
```

|variable|Purpose|Member of|Sample Values|
|--------|------------------|----------|--------|
|id|id for the menu and should be unique|parent|"GADomainMenu"|
|description|Description shown as subtitle in the menu card|parent|"Google Analytics"|
|action|when user clicks on it what action to be performed|parent|"menu" or "intent"|
|menuItems|An array of menu items to be shown in case action is menu|parent|-|
|id|id for the menu item|menuItems|"Usage"|
|optionText|Menu Option text to be shown|menuItems|"Usage Details"|
|intent|intent to be invoked, in case action is intent|parent|"usage"|
|entities|Default entities array to be filled in case action is intent.Optional|parent|-|
|type|entity name to be filled|entities|"location"|
|value|entity value to be filled|entities|"chennai"|
||||

# global-intents.json

```
Global commands invokes global intents and in turn dialogs. 

This config defines all the global intents and their dialog starting point. 

```

Below definition is an array

|variable|Purpose        |Sample Values|
|--------|---------------|------------|
|name|global intent name|"apps", "GoodBye"|
|botDialog|Dialog to be called when command given|"/apps" "/goodBye"|
|entryPoint|conversation json name. Optional|'/purpleSlate/help/global_help.json'|
|description|what appears when typing **help commands**|"**goodbye** or **bye** - To end conversation"|
|userDefined|Whether command can be shown to user in **help commands**|true or false|
|regExp|RegEx to match the input command of the user|"^help" for anything starts with help|
|flags|Flags to be used in regEx|"i" for ignore case|
|||

For **GoodBye** and **Help** commands, utterances are taken from the input variations defined in the chatters as well. 

# dsvDefinition.json

```
This is to add spelling intelligence to domain specific words. 

In case the NLP engine can not identify the incoming text, Bot will check each word in the incoming text with DSV definition list to see how close they are. fuzzyset is the javascript utility which ranks the closeness to the word. It would not check for stop words. 

Based on the high ranked word, Bot replaces that in utterance and again checks with NLP engine on whether it can find the intent. 
```

|variable|Purpose        |Member of|Sample Values|
|--------|---------------|----------|-------|
|stopWords|An array of stop words |parent|["is","what","who"]|
|dsvs|Array of dsvs for all domain|parent|-|
|domain|domain Id for which dsvlist is defined|dsvs|'GoogleAnalytics'|
|dsvList|Array of domain specific words|dsvs|["usage","user","exception"]|
||||

# conversationDefinition.json

```
This is where the conversation flows are defined and mapped with each other. 

This collection in mongoDB can also be created by running the 'src\utilities\importConversation.ts' file, 

which takes the data from the resources directory present in ps-bot.

```
|variable|Purpose        |Member of|Sample Values|
|--------|---------------|----------|-------|
|conversationId|conversation id or name|parent|'GoogleAnalytics.usage.ga_1'|
|msgs|This is an object array that can contain multiple messages that will be taken to the user one by one|parent||
|msgType|This holds the type of message to be displayed. These types are given below. |msgs|Text,cards and many more|
||||

The below table shows the currently supported message types

|msgType|purpose|parameters to follow|sample|
|----------|-----|------|---------------------|
|Text|simple text reply|msgText|Your Outstanding Balance as of today is **${ccBalanceData.accountSummary.currentBalance}$**|
|Choice|Provides a list of choices to choose from|msgText|Choose from the below list|
|||choices|["option1","option2","option3"] or ${jiraGetProjects.response.projectNameArray[]}$ (This returns an array of strings) or if it is an object array ${jiraGetProjects.response.keys[].key}$ (this returns an array of objects containing a parameter name 'key') |
|||listStyle|List style representing integer [link](https://docs.microsoft.com/en-us/azure/bot-service/nodejs/bot-builder-nodejs-dialog-prompt?view=azure-bot-service-3.0#promptschoice)|
|||responses|This is an array of array. The inner array contains two elements. Index '0' contains an option name and Index '1' must have the conversation id to be pointed next.[["option1","option1.conversationID"],["option2","option2.conversationID"]]|
|||assignTo|The response will be saved to this assignTo value. 'psEntity.projectName'|
|Chatter|To get data from our my-sql chatters|domain|Domain name from sql like "Enginiri", "sales","exception"|
|||context|Context to choose the chatter from. Ex: "suggestion","greeting","nlu"|
|Recommend|Access ps-Recomendation service to get recommendations|domain||
|||intent||
|||tags||
|Card|To display image [cards](https://www.google.co.in/search?q=botframework+cards&rlz=1C1CHBF_enIN802IN802&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjytbDpmJ_eAhUJso8KHXITDZsQ_AUIDigB&biw=1366&bih=657#imgrc=suxxOKgRQhEo1M:)|title|title of the card|
|||subtitle|Subtitle of the card|
|||image|image for the card|
|||buttonUrl|url endpoint for the button|
|||buttonText|button over text|
|||contentType||
|||contentUrl||
|||responses||
|||||

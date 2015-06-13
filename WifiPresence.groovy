metadata {
	// Automatically generated. Make future change here.
	// Modified from SmartThings "Presence" template
    
	definition (name: "Wifi Presence", namespace: "schettino", author: "John Schettino") {
		capability "Presence Sensor"
		capability "Sensor"
        
        command "setPresence"
        command "currentPresence"
        
	}
    
	simulator {
		status "present": "presence: 1"
		status "not present": "presence: 0"
	}

	tiles {
		standardTile("presence", "device.presence", width: 2, height: 2, canChangeBackground: true) {
			state("present", labelIcon:"st.presence.tile.mobile-present", backgroundColor:"#53a7c0")
			state("not present", labelIcon:"st.presence.tile.mobile-not-present", backgroundColor:"#ffffff")
		}
		main "presence"
		details "presence"
	}
}


def setPresence(String v) {	
	if (v == "not") v = "not present"
    
    def old = currentPresence()
    
    // log.debug "$device.displayName  presence: $v old $old" 
    
    // do nothing if already in that state
	if ( old != v) {
	    sendEvent(displayed: true,  isStateChange: true, name: "presence", value: v, descriptionText: "$device.displayName is $v")
	} 
}

def String currentPresence() {
	return device.latestValue("presence")
}


def parse(String description) {
	def name = parseName(description)
	def value = parseValue(description)
	def linkText = getLinkText(device)
	def descriptionText = parseDescriptionText(linkText, value, description)
	def handlerName = getState(value)
	def isStateChange = isStateChange(device, name, value)

	def results = [
		name: name,
		value: value,
		unit: null,
		linkText: linkText,
		descriptionText: descriptionText,
		handlerName: handlerName,
		isStateChange: isStateChange,
		displayed: displayed(description, isStateChange)
	]
	log.debug "Parse returned $results.descriptionText"
	return results

}

private String parseName(String description) {
	if (description?.startsWith("presence: ")) {
		return "presence"
	}
	null
}

private String parseValue(String description) {
	switch(description) {
		case "presence: 1": return "present"
		case "presence: 0": return "not present"
		default: return description
	}
}

private parseDescriptionText(String linkText, String value, String description) {
	switch(value) {
		case "present": return "$linkText has arrived"
		case "not present": return "$linkText has left"
		default: return value
	}
}

private getState(String value) {
	switch(value) {
		case "present": return "arrived"
		case "not present": return "left"
		default: return value
	}
}


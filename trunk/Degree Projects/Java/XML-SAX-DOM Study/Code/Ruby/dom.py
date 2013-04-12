from xml.dom import minidom
dom = minidom.parse('25muebles.xml')

habitaciones = dom.getElementsByTagName('habitacion')

for i in range(0,len(habitaciones)):
	print habitaciones[i].attributes["id"].nodeValue


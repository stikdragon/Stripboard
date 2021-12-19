package uk.co.stikman.strip.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import uk.co.stikman.strip.client.model.components.ComponentCapacitor;
import uk.co.stikman.strip.client.model.components.ComponentDip;
import uk.co.stikman.strip.client.model.components.ComponentResistor;
import uk.co.stikman.strip.client.model.components.ComponentWire;
import uk.co.stikman.strip.client.util.Ini;
import uk.co.stikman.strip.client.util.IniPair;
import uk.co.stikman.strip.client.util.Section;

public class ComponentLibrary {
	private Map<String, Component> components = new HashMap<>();

	public void loadFrom(String source) {
		//
		// add default Wire type
		//
		Component comp = new ComponentWire("HIDDEN", "Wire");
		comp.getPins().add(new Pin(comp, 0, 0, 0));
		comp.getPins().add(new Pin(comp, 0, 0, 0));
		components.put(comp.getName(), comp);

		Ini ini = new Ini(false, true);
		ini.parse(source);
		for (Section sect : ini.getSections()) {
			//@formatter:off
			ComponentType typ;
			String s = sect.get("type");
			switch (s.toLowerCase()) {
			case "ic_dip":	   typ = ComponentType.IC_DIP; 	break;
			case "r":          typ = ComponentType.R;       break;
			case "c-radial":   typ = ComponentType.C_RADIAL;break;
			case "c-axial":    typ = ComponentType.C_AXIAL; break;
			case "c-disc":     typ = ComponentType.C_DISC;  break;
			case "led":        typ = ComponentType.LED;     break;
			case "to92":       typ = ComponentType.TO92;    break;
			case "to220":      typ = ComponentType.TO220;   break;
			default: throw new NoSuchElementException("Component type [" + s + "] not recognised");
			}
			//@formatter:on

			String[] bits = sect.getName().split("/");
			String grup = bits.length == 2 ? bits[0] : null;
			String name = bits.length == 2 ? bits[1] : bits[0];

			switch (typ) {
			case IC_DIP:
				comp = new ComponentDip(grup, name);
				break;
			case R:
				comp = new ComponentResistor(grup, name);
				break;
			case C_AXIAL:
			case C_RADIAL:
			case C_DISC:
				comp = new ComponentCapacitor(grup, name, typ);
				break;
			default:
				throw new NoSuchElementException("component type [" + typ + "] not supported");
			}

			comp.setDesc(comp.getName());
			if (sect.containsKey("desc"))
				comp.setDesc(sect.get("desc"));

			for (IniPair k : sect) {
				if (k.getName().matches("[0-9]+")) {
					int n = Integer.parseInt(k.getName());
					while (n >= comp.getPins().size())
						comp.getPins().add(new Pin(comp, comp.getPins().size()));
					bits = k.getValue().split(",");
					if (bits.length != 2 && bits.length != 3)
						throw new IllegalArgumentException("Expected two or three arguments for a pin");
					comp.getPins().get(n).getPosition().set(Integer.parseInt(bits[0]), Integer.parseInt(bits[1]));
					if (bits.length > 2)
						comp.getPins().get(n).setName(bits[2]);
				}
			}
			comp.calcSize();
			comp.setPrefix(sect.containsKey("prefix") ? sect.get("prefix") : "X");
			components.put(comp.getName(), comp);
		}

	}

	public Iterable<Component> getComponents() {
		return components.values();
	}

	public Component get(String name) {
		Component x = components.get(name);
		if (x == null)
			throw new NoSuchElementException(name + " not a component");
		return x;
	}

}

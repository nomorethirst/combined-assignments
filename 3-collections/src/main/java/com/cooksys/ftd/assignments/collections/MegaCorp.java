package com.cooksys.ftd.assignments.collections;

import com.cooksys.ftd.assignments.collections.hierarchy.Hierarchy;
import com.cooksys.ftd.assignments.collections.model.Capitalist;
import com.cooksys.ftd.assignments.collections.model.FatCat;
import com.cooksys.ftd.assignments.collections.model.WageSlave;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class MegaCorp implements Hierarchy<Capitalist, FatCat> {
    private HashSet<Capitalist> capSet = new HashSet<Capitalist>();

    /**
     * Adds a given element to the hierarchy.
     * <p>
     * If the given element is already present in the hierarchy,
     * do not add it and return false
     * <p>
     * If the given element has a parent and the parent is not part of the hierarchy,
     * add the parent and then add the given element
     * <p>
     * If the given element has no parent but is a Parent itself,
     * add it to the hierarchy
     * <p>
     * If the given element has no parent and is not a Parent (instanceof Capitalist) itself,
     * do not add it and return false
     *
     * @param capitalist the element to add to the hierarchy
     * @return true if the element was added successfully, false otherwise
     */
    @Override
    public boolean add(Capitalist capitalist) {
	
	// Check doesn't exist and is right type
	if (has(capitalist) || !(capitalist instanceof Capitalist))
	    return false;

	// Check not ParentlessWageSlave
	if (capitalist instanceof WageSlave && !capitalist.hasParent())
	    return false;
	
	// Add all parents in parentChain recursively
	if (capitalist.hasParent())
	    add(capitalist.getParent());

	// After all checks add to capSet
	if (capSet.add(capitalist))
	    return true;
	else
	    return false;
	
    }

    /**
     * @param capitalist the element to search for
     * @return true if the element has been added to the hierarchy, false otherwise
     */
    @Override
    public boolean has(Capitalist capitalist) {
	if (capSet.contains(capitalist))
	    return true;
	else
	    return false;
    }

    /**
     * @return all elements in the hierarchy,
     * or an empty set if no elements have been added to the hierarchy
     */
    @Override
    public Set<Capitalist> getElements() {
	return capSet.stream().collect(Collectors.toSet());
    }

    /**
     * @return all parent elements in the hierarchy,
     * or an empty set if no parents have been added to the hierarchy
     */
    @Override
    public Set<FatCat> getParents() {
	
	// Init return type - Set of Parents
        Set<FatCat> parents = new HashSet<>();
        
        // Loop through capSet, test for parents, add all parents in chain
        for (Capitalist cap : capSet) {
            FatCat parent = cap instanceof FatCat ? (FatCat) cap : cap.getParent();
            while (parent != null) {
                parents.add(parent);
                parent = parent.getParent();
            }
        }
        return parents;
    }

    /**
     * @param fatCat the parent whose children need to be returned
     * @return all elements in the hierarchy that have the given parent as a direct parent,
     * or an empty set if the parent is not present in the hierarchy or if there are no children
     * for the given parent
     */
    @Override
    public Set<Capitalist> getChildren(FatCat fatCat) {
	
	// Init return type - Empty HashSet
	HashSet<Capitalist> children = new HashSet<Capitalist>();
	
	// Return empty set if arg fatCat not in capSet
	if (!has(fatCat))
	    return children;
	
	// Loop through capSet and add caps if parent is arg fatCat
	for (Capitalist cap: capSet) {
	    if (cap.getParent() == fatCat)
		children.add(cap);
	}
	return children;
    }

    /**
     * @return a map in which the keys represent the parent elements in the hierarchy,
     * and the each value is a set of the direct children of the associate parent, or an
     * empty map if the hierarchy is empty.
     */
    @Override
    public Map<FatCat, Set<Capitalist>> getHierarchy() {

	// Init return type - Map of parents to sets of their children
	HashMap<FatCat, Set<Capitalist>> hierarchy = new HashMap<FatCat, Set<Capitalist>>();
	
	// Get the parents
	Set<FatCat> parents = getParents();
	
	// Loop through parents, getting children and adding to hashmap
	for(FatCat parent: parents) {
	    Set<Capitalist> children = getChildren(parent);
	    hierarchy.put(parent,  children);
	}

	return hierarchy;
    }

    /**
     * @param capitalist
     * @return the parent chain of the given element, starting with its direct parent,
     * then its parent's parent, etc, or an empty list if the given element has no parent
     * or if its parent is not in the hierarchy
     */
    @Override
    public List<FatCat> getParentChain(Capitalist capitalist) {

	// Init return type - List, return empty if arg is null
	List<FatCat> parentChain = new ArrayList<FatCat>();
	if (capitalist == null)
	    return parentChain;

	// Loop through parent chain and add to list
	while (capitalist.hasParent() && has(capitalist.getParent())) {
	    parentChain.add(capitalist.getParent());
	    capitalist = capitalist.getParent();
	}

	return parentChain;
    }
}
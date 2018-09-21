package com.example.bhavin.anroidstudiophotos68;
/*
 * @author Bhavin Patel
 * @author Akshar Patel
 */

import java.io.Serializable;

public class TagIdentity implements  Serializable, Comparable<TagIdentity>
{
    private static final long serialVersionUID = 1L;
    String name , value;

    /*
     * constructure takes tag name and its value
     */

    public TagIdentity()
    {

    }
    public TagIdentity(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    /*
     * return name of the tag
     */
    public String getName()
    {
        return name;
    }

    /*
     * set tag name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /*
     * returns tag value
     */
    public String getValue()
    {
        return value;
    }

    /*
     * set value of the tag
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return name + " : " + value;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if (o == null || !( o instanceof TagIdentity))
        {
            return false;
        }
        TagIdentity object2 = (TagIdentity)(o);

        if(object2.getName().equals(""))
        {
            return object2.name.equalsIgnoreCase(this.name);
        }

        if(object2.getValue().equals(""))
        {
            return object2.value.equalsIgnoreCase(this.value);
        }

        return object2.name.equalsIgnoreCase(this.name)
                && object2.value.equalsIgnoreCase(this.value);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(TagIdentity arg0)
    {
        if (this.name.equalsIgnoreCase(arg0.name))
        {
            return this.name.compareTo(arg0.name);
        }
        else
        {
            return this.value.compareTo(arg0.value);
        }


    }


}


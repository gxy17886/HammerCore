package com.mrdimka.hammercore.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.TextFormatting;

/**
 * @deprecated use {@link TextFormatting} instead.
 **/
@Deprecated
public enum ChatColor
{
	BLACK('0'), DARK_BLUE('1'), DARK_GREEN('2'), DARK_AQUA('3'), DARK_RED('4'), DARK_PURPLE('5'), GOLD('6'), GRAY('7'), DARK_GRAY('8'), BLUE('9'), GREEN('a'), AQUA('b'), RED('c'), LIGHT_PURPLE('d'), YELLOW('e'), WHITE('f'), OBFUSCATED('k', true), BOLD('l', true), STRIKETHROUGH('m', true), UNDERLINE('n', true), ITALIC('o', true), RESET('r');
	
	public static final char PREFIX_CODE = '\u00A7';
	private static final Map<Character, ChatColor> FORMATTING_BY_CHAR;
	private static final Map<String, ChatColor> FORMATTING_BY_NAME;
	private static final Pattern STRIP_FORMATTING_PATTERN;
	private final char code;
	private final boolean isFormat;
	private final String toString;
	
	private ChatColor(char code)
	{
		this(code, false);
	}
	
	private ChatColor(char code, boolean isFormat)
	{
		this.code = code;
		this.isFormat = isFormat;
		toString = PREFIX_CODE + "" + code;
	}
	
	static
	{
		FORMATTING_BY_CHAR = new HashMap();
		FORMATTING_BY_NAME = new HashMap();
		STRIP_FORMATTING_PATTERN = Pattern.compile("(?i)" + PREFIX_CODE + "[0-9A-FK-OR]");
		for(ChatColor format : values())
		{
			FORMATTING_BY_CHAR.put(Character.valueOf(format.getChar()), format);
			FORMATTING_BY_NAME.put(format.getName(), format);
		}
	}
	
	public char getChar()
	{
		return this.code;
	}
	
	public boolean isFormat()
	{
		return this.isFormat;
	}
	
	public boolean isColor()
	{
		return (!this.isFormat) && (this != RESET);
	}
	
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public String toString()
	{
		return PREFIX_CODE + "" + getChar();
	}
	
	public static String stripFormatting(String input)
	{
		return input == null ? null : STRIP_FORMATTING_PATTERN.matcher(input).replaceAll("");
	}
	
	public static ChatColor getByChar(char code)
	{
		return (ChatColor) FORMATTING_BY_CHAR.get(Character.valueOf(code));
	}
	
	public static ChatColor getByName(String name)
	{
		if(name == null)
			return null;
		return (ChatColor) FORMATTING_BY_NAME.get(name.toLowerCase());
	}
	
	public static Collection<String> getNames(boolean getColors, boolean getFormats)
	{
		List<String> result = new ArrayList();
		for(ChatColor format : values())
		{
			if(((!format.isColor()) || (getColors)) && ((!format.isFormat()) || (getFormats)))
			{
				result.add(format.getName());
			}
		}
		return result;
	}
}
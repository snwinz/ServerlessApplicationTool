package antlr;

import org.antlr.v4.runtime.Token;

public class TokenContent {
	private Token token;
	private String text;

	public TokenContent(Token token, String text) {
		this.token = token;
		this.text = text;
	}

	public Token getToken() {
		return token;
	}

	public String getText() {
		return text;
	}
}

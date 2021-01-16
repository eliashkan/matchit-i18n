package be.fedasil.app;

import picocli.CommandLine;

@CommandLine.Command(
		name = "i18n",
		description = "matchit dev i18n increment helper",
		mixinStandardHelpOptions = true,
		version = "matchit-i18n 1.0",
		subcommands = {
				ParseIncoming.class,
				GenerateOutgoing.class
		})
public class I18nApp {
	
	public static void main(String... args) {
		int exitCode = new CommandLine(new I18nApp()).execute(args);
		System.exit(exitCode);
	}
}

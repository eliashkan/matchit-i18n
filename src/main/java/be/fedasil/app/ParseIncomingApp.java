package be.fedasil.app;

import static picocli.CommandLine.Command;

@Command(
		name = "matchit-i18n-generate-outgoing",
		mixinStandardHelpOptions = true,
		version = "matchit-i18n 1.0",
		description = "Parse incoming i18n files reviewed by helpdesk."
)
public class ParseIncomingApp {
}

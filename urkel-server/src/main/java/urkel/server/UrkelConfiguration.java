package urkel.server;

import com.github.jknack.handlebars.Handlebars;
import lombok.Getter;
import lombok.Setter;
import org.cobbzilla.mail.sender.SmtpMailConfig;
import org.cobbzilla.util.handlebars.HandlebarsUtil;
import org.cobbzilla.util.javascript.StandardJsEngine;
import org.cobbzilla.wizard.cache.redis.HasRedisConfiguration;
import org.cobbzilla.wizard.cache.redis.RedisConfiguration;
import org.cobbzilla.wizard.server.config.DatabaseConfiguration;
import org.cobbzilla.wizard.server.config.HasDatabaseConfiguration;
import org.cobbzilla.wizard.server.config.RestServerConfiguration;
import org.springframework.context.annotation.Bean;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

public class UrkelConfiguration extends RestServerConfiguration implements HasDatabaseConfiguration, HasRedisConfiguration {

    @Setter private DatabaseConfiguration database;
    @Bean public DatabaseConfiguration getDatabase() { return database; }

    @Getter @Setter private SmtpMailConfig smtp;

    @Setter private RedisConfiguration redis;
    @Bean public RedisConfiguration getRedis() {
        if (redis == null) redis = new RedisConfiguration();
        if (empty(redis.getPrefix())) redis.setPrefix(getId());
        return redis;
    }

    @Getter(lazy=true) private final Handlebars handlebars = initHandlebars();
    private Handlebars initHandlebars() {
        final Handlebars hbs = new Handlebars(new HandlebarsUtil(getClass().getSimpleName()));
        HandlebarsUtil.registerUtilityHelpers(hbs);
        HandlebarsUtil.registerDateHelpers(hbs);
        HandlebarsUtil.registerJavaScriptHelper(hbs, StandardJsEngine::new);
        return hbs;
    }

}

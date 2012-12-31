package com.us.fountainhead.gifnoc.server.test;

import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.annotations.spring.UsingSpring;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.junit.spring.SpringAnnotatedEmbedderRunner;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.List;

/**
 * Runs the story tests
 */
@RunWith(SpringAnnotatedEmbedderRunner.class)
@Configure
@UsingEmbedder(embedder = Embedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = false, stepsFactory = true)
@UsingSpring(resources = "classpath:context.xml")
@UsingSteps
public class Stories extends JUnitStories {

    protected List<String> storyPaths() {
        URL url = CodeLocations.codeLocationFromPath("server-at/src/test/resources");
        List<String> storyList = new StoryFinder().findPaths(url, "stories/*.story", "");
        if(storyList.isEmpty()) {
            url = CodeLocations.codeLocationFromPath("src/test/resources");
            storyList = new StoryFinder().findPaths(url, "stories/*.story", "");
        }

        return storyList;}


}

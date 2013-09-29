<h1>Diploma Generator</h1>

<p>Create beautiful diploma, certificate or award of excellence using
	this free online generator. It is fully customizable and the output is
	a simple PDF file ready to print (or you can send emails to your
	customers via email).</p>

<p>
	<strong>If you don't want to hassle with creating and maintaining a Java EE
	server and just want to use this diploma generator, goto <a
		href="http://www.dipgen.com" target="_blank">http://www.dipgen.com</a></strong>
</p>

<h2>If you do want to play with Dipgen on your Java EE server:</h2>

<h3>To run in development mode (using embedded hsql database):</h3>

<p>
first run: <code>mvn clean package</code><br />
next run: <code>mvn jetty:run -Djetty.port=8080 -Dspring.profiles.active="dev"</code>
</p>

<p>This will start embedded Jetty server on port 8080 and you can access your application here: <code>http://localhost:8080</code></p>

<p>
These users are available: <code>user / password</code>:<br />
<code>admin / admin</code><br />
<code>guest / guest</code>
</p>

<h3>To deploy on Heroku:</h3>

<p>
first run: <code>mvn package</code><br />
next goto target directory and run:<code>heroku deploy:war --war dipgen.war --app YOUR_APP</code><br />
To deploy WAR to Heroku see this article: <a href="https://devcenter.heroku.com/articles/war-deployment">https://devcenter.heroku.com/articles/war-deployment</a><br />

This user is available: <code>user / password</code>:<br />
<code>admin / admin</code>
</p>

<h2>My other projects:</h2>

<ul>
	<li>
		<a href="http://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)
	</li>
    <li>
		<a href="http://sitemonitoring.sourceforge.net/" target="_blank" title="Website monitoring software">Website monitoring software</a> (free OSS software)
	</li>
    <li>
		<a href="http://www.java-skoleni.cz" target="_blank" title="Java školené">Java školení</a> (in Czech)
	</li>
    <li>
		<a href="http://www.sql-skoleni.cz" target="_blank" title="Java školení">SQL školení</a> (in Czech)
	</li>
</ul>

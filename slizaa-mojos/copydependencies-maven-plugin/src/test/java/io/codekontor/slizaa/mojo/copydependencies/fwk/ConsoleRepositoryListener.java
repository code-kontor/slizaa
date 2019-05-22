/**
 * copydependencies-maven-plugin - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.mojo.copydependencies.fwk;

import org.eclipse.aether.AbstractRepositoryListener;
import org.eclipse.aether.RepositoryEvent;

/**
 * A simplistic repository listener that logs events to the console.
 */
public class ConsoleRepositoryListener
    extends AbstractRepositoryListener
{

  @Override
  public void artifactDeployed(RepositoryEvent event)
  {
    System.out.println("Deployed " + event.getArtifact() + " to " + event.getRepository());
  }

  @Override
  public void artifactDeploying(RepositoryEvent event)
  {
    System.out.println("Deploying " + event.getArtifact() + " to " + event.getRepository());
  }

  @Override
  public void artifactDescriptorInvalid(RepositoryEvent event)
  {
    System.out.println("Invalid artifact descriptor for " + event.getArtifact() + ": "
        + event.getException().getMessage());
  }

  @Override
  public void artifactDescriptorMissing(RepositoryEvent event)
  {
    System.out.println("Missing artifact descriptor for " + event.getArtifact());
  }

  @Override
  public void artifactInstalled(RepositoryEvent event)
  {
    System.out.println("Installed " + event.getArtifact() + " to " + event.getFile());
  }

  @Override
  public void artifactInstalling(RepositoryEvent event)
  {
    System.out.println("Installing " + event.getArtifact() + " to " + event.getFile());
  }

  @Override
  public void artifactResolved(RepositoryEvent event)
  {
    System.out.println("Resolved artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  @Override
  public void artifactDownloading(RepositoryEvent event)
  {
    System.out.println("Downloading artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  @Override
  public void artifactDownloaded(RepositoryEvent event)
  {
    System.out.println("Downloaded artifact " + event.getArtifact() + " from " + event.getRepository());
  }

  @Override
  public void artifactResolving(RepositoryEvent event)
  {
    System.out.println("Resolving artifact " + event.getArtifact());
  }

  @Override
  public void metadataDeployed(RepositoryEvent event)
  {
    System.out.println("Deployed " + event.getMetadata() + " to " + event.getRepository());
  }

  @Override
  public void metadataDeploying(RepositoryEvent event)
  {
    System.out.println("Deploying " + event.getMetadata() + " to " + event.getRepository());
  }

  @Override
  public void metadataInstalled(RepositoryEvent event)
  {
    System.out.println("Installed " + event.getMetadata() + " to " + event.getFile());
  }

  @Override
  public void metadataInstalling(RepositoryEvent event)
  {
    System.out.println("Installing " + event.getMetadata() + " to " + event.getFile());
  }

  @Override
  public void metadataInvalid(RepositoryEvent event)
  {
    System.out.println("Invalid metadata " + event.getMetadata());
  }

  @Override
  public void metadataResolved(RepositoryEvent event)
  {
    System.out.println("Resolved metadata " + event.getMetadata() + " from " + event.getRepository());
  }

  @Override
  public void metadataResolving(RepositoryEvent event)
  {
    System.out.println("Resolving metadata " + event.getMetadata() + " from " + event.getRepository());
  }
}

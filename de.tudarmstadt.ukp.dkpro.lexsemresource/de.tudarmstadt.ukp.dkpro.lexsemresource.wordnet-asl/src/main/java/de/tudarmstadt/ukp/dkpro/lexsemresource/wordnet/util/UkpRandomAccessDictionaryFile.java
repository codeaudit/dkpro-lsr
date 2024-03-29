/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.tudarmstadt.ukp.dkpro.lexsemresource.wordnet.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.file.DictionaryFile;
import net.didion.jwnl.dictionary.file.DictionaryFileType;
import net.didion.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile;
import de.tudarmstadt.ukp.dkpro.lexsemresource.core.util.FileUtils;

public
class UkpRandomAccessDictionaryFile
extends	PrincetonRandomAccessDictionaryFile
{
	private POS _pos;
	/**
	 * The type of the file. For example, the default implementation defines the
	 * types INDEX, DATA, and EXCEPTION.
	 */
	private DictionaryFileType _fileType;
	private File _file;
	private URL _url;

	public
	UkpRandomAccessDictionaryFile()
	{
		// Nothing to do.
	}

	public
	UkpRandomAccessDictionaryFile(
			String path,
			POS pos,
			DictionaryFileType fileType)
	{
		this(path, pos, fileType, READ_ONLY);
	}

	@Override
	public
	DictionaryFile newInstance(
			String path,
			POS pos,
			DictionaryFileType fileType)
	{
		return new UkpRandomAccessDictionaryFile(path, pos, fileType);
	}

	public
	UkpRandomAccessDictionaryFile(
			String path,
			POS pos,
			DictionaryFileType fileType,
			String permissions)
	{
		_pos = pos;
		_fileType = fileType;
		_permissions = permissions;

        String searchUrl = path.replace('\\', '/');
        if (!searchUrl.startsWith("/")) {
        	searchUrl = "/" + searchUrl;
        }
        if (!searchUrl.endsWith("/")) {
        	searchUrl += "/";
        }

		_url = getClass().getResource(searchUrl+makeFilename());
		if (_url == null) {
			_file = new File(path, makeFilename());
		}
	}

	/** The POS associated with this file.*/
	@Override
	public POS getPOS() {
		return _pos;
	}

	@Override
	public File getFile() {
		return _file;
	}

	/** The file type associated with this file.*/
	@Override
	public DictionaryFileType getFileType() {
		return _fileType;
	}

	/** Open the file. */
	@Override
	public void	open() throws IOException {
		if (!isOpen()) {
			if (_url != null && _file == null) {
				_file = FileUtils.getUrlAsFile(_url, true);
			}
		}
			openFile(_file);
	}
}
